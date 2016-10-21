package expression.exceptions;

/**
 * Created by aydar on 11.04.16.
 */
public class CheckedSqrt implements TripleExpression {
    final private TripleExpression expression;

    public CheckedSqrt(TripleExpression expression) {
        if (expression == null) {
            throw new NullPointerException();
        }
        this.expression = expression;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int s = expression.evaluate(x, y, z);
        if (s < 0) {
            throw new EvaluateException("Sqrt from negative number attempt.");
        }
        int l = 0;
        int r = s + 1;
        while (l + 1 < r) {
            int m = (l + r) / 2;
            if (m <= s / m) {
                l = m;
            } else {
                r = m;
            }
        }
        return l;
    }
}
