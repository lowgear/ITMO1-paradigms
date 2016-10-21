package expression.exceptions;

/**
 * Created by aydar on 14.03.16.
 */
public class Variable implements TripleExpression {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "x": return x;
            case "y": return y;
            case "z": return z;
        }
        throw new EvaluateException("Unrecognized variable name.");
    }
}
