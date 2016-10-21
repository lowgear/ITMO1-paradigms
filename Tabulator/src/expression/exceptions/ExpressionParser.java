package expression.exceptions;

import expression.generic.GenNumber;

/**
 * Created by aydar on 04.04.16.
 */
public class ExpressionParser<T extends GenNumber> implements Parser {
    private int index;
    private String var;
    private T num;
    private final int maxPriority = 2;
    private String exp;

    /*String[] operatorRep = {"sqrt", "abs", "**", "//"};
    String[] operatorUse = {"s", "a", "p", "l"};*/
    private enum Operation {
        PLUS, MINUS, MUL, DIV, POW, LOG, NOTHING
    }

    private enum Identificator {NUM, VAR, LBRACE, RBRACE, SQRT, ABS, NEGATE, NOTHING}

    @Override
    public TripleExpression parse(String expression) {
        index = 0;
        exp = expression;

        /*String trash = expression;
        for (String match : operatorRep) {
            trash = trash.replaceAll(match, "");
        }
        for (String match : operatorUse) {
            if (trash.contains(match)) {
                throw new expression.exceptions.ParseException("Illegal lexemes in expression.");
            }
        }
        for (int i = 0; i < operatorRep.length; i++) {
            exp.replaceAll(operatorRep[i], operatorUse[i]);
        }*/

        TripleExpression tripleExp = binOps(0);
        if (index < expression.length()) {
            throwParserExceptionWithMessage("Extra symbols found.");
        }
        return tripleExp;
    }

    private void throwParserExceptionWithMessage(String message) {
        throw new ParseException(getPositionStrRep() + message);
    }

    private TripleExpression binOps(int priority) {
        if (priority > maxPriority) {
            return prim();
        }
        TripleExpression tripleExp = binOps(priority + 1);
        for (; ; ) {
            Operation op = getOperation();
            if (getOperationPriority(op) != priority) {
                putOperationBack(op);
                return tripleExp;
            }
            TripleExpression nextExp = binOps(priority + 1);
            switch (op) {
                case PLUS:
                    tripleExp = new CheckedAdd(tripleExp, nextExp);
                    break;
                case MINUS:
                    tripleExp = new CheckedSubtract(tripleExp, nextExp);
                    break;
                case MUL:
                    tripleExp = new CheckedMultiply(tripleExp, nextExp);
                    break;
                case DIV:
                    tripleExp = new CheckedDivide(tripleExp, nextExp);
                    break;
                case POW:
                    tripleExp = new CheckedPower(tripleExp, nextExp);
                    break;
                case LOG:
                    tripleExp = new CheckedLogarithm(tripleExp, nextExp);
                    break;
            }
        }
    }

    private int getOperationPriority(Operation op) {
        switch (op) {
            case PLUS:
            case MINUS:
                return 0;
            case MUL:
            case DIV:
                return 1;
            case POW:
            case LOG:
                return 2;
            default:
                return -1;
        }
    }

    private void putOperationBack(Operation op) {
        switch (op) {
            case PLUS:
            case MINUS:
            case MUL:
            case DIV:
                index -= 1;
                break;
            case POW:
            case LOG:
                index -= 2;
                break;
            case NOTHING:
                break;
        }
    }

    private Operation getOperation() {
        passWhitespace();
        if (index + 2 <= exp.length()) {
            switch (exp.substring(index, index + 2)) {
                case "**":
                    index += 2;
                    return Operation.POW;
                case "//":
                    index += 2;
                    return Operation.LOG;
            }
        }
        if (index + 1 <= exp.length()) {
            switch (exp.substring(index, index + 1)) {
                case "*":
                    index += 1;
                    return Operation.MUL;
                case "/":
                    index += 1;
                    return Operation.DIV;
                case "+":
                    index += 1;
                    return Operation.PLUS;
                case "-":
                    index += 1;
                    return Operation.MINUS;
            }
        }
        return Operation.NOTHING;
    }

    private TripleExpression prim() {
        Identificator id = getIdentificator();
        switch (id) {
            case NUM:
                return new Const(num);
            case VAR:
                TripleExpression tripleExp = new Variable(var);
                var = null;
                return tripleExp;
            case LBRACE:
                tripleExp = binOps(0);
                if (getIdentificator() != Identificator.RBRACE) {
                    throwParserExceptionWithMessage("Closing brace expected, but not found.");
                }
                return tripleExp;
            case SQRT:
                return new CheckedSqrt(prim());
            case ABS:
                return new CheckedAbs(prim());
            case NEGATE:
                return new CheckedNegate(prim());
            default:
                throwParserExceptionWithMessage("Primary expression expected, but nothing found.");
        }
        return null;
    }

    private String getPositionStrRep() {
        return "At position " + index + ": ";
    }

    private Identificator getIdentificator() {
        if (passWhitespace()) {
            return Identificator.NOTHING;
        }

        switch (exp.charAt(index)) {
            case '(':
                index++;
                return Identificator.LBRACE;
            case ')':
                index++;
                return Identificator.RBRACE;
        }
        if (Character.isAlphabetic(exp.charAt(index))) {
            if (checkMatchAtIndex("sqrt")) {
                index += "sqrt".length();
                return Identificator.SQRT;
            }
            if (checkMatchAtIndex("abs")) {
                index += "abs".length();
                return Identificator.ABS;
            }
            var = exp.substring(index, index + 1);
            if (var.compareTo("x") != 0 && var.compareTo("y") != 0 && var.compareTo("z") != 0) {
                throwParserExceptionWithMessage("Invalid variable var.");
            }
            index++;
            return Identificator.VAR;
        }

        {
            String sign = "";
            if (exp.charAt(index) == '-') {
                sign = "-";
                index++;
                if (passWhitespace()) {
                    return Identificator.NOTHING;
                }
                if (!Character.isDigit(exp.charAt(index))) {
                    return Identificator.NEGATE;
                }
            }
            int begin = index;
            if (Character.isDigit(exp.charAt(index))) {
                while (index < exp.length() && Character.isDigit(exp.charAt(index))) {
                    index++;
                }
                try {
                    num = T.parseInt(sign + exp.substring(begin, index));
                    return Identificator.NUM;
                } catch (NumberFormatException e) {
                    throwParserExceptionWithMessage("Number expected but not found.");
                }
            }
        }
        return Identificator.NOTHING;
    }

    private boolean checkMatchAtIndex(String tar) {
        return exp.regionMatches(index, tar, 0, tar.length());
    }

    private boolean passWhitespace() {
        while (index < exp.length() && Character.isWhitespace(exp.charAt(index))) {
            index++;
        }
        return index == exp.length();
    }
}
