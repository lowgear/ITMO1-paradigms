package expression.exceptions;

/**
 * Created by aydar on 11.04.16.
 */
public class CheckedAbs implements TripleExpression {
    final private TripleExpression expression;

    public CheckedAbs(TripleExpression expression) {
        if (expression == null) {
            throw new NullPointerException();
        }
        this.expression = expression;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int res = expression.evaluate(x, y, z);
        if (res >= 0) {
            return res;
        }
        if (res == Integer.MIN_VALUE)
            throw new EvaluateException("Abs from INT_MIN attempt.");
        return -res;
    }
}
