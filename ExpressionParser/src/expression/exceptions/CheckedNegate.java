package expression.exceptions;

/**
 * Created by aydar on 04.04.16.
 */
public class CheckedNegate implements TripleExpression {
    final private TripleExpression expression;

    public CheckedNegate(TripleExpression expression) {
        if (expression == null) {
            throw new NullPointerException();
        }
        this.expression = expression;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int res = expression.evaluate(x, y, z);
        if (res == Integer.MIN_VALUE)
            throw new EvaluateException("Negate of MIN_INT attempt.");
        return -res;
    }
}
