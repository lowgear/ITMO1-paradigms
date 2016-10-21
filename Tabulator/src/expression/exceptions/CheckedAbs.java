package expression.exceptions;

import expression.generic.GenNumber;

/**
 * Created by aydar on 11.04.16.
 */
public class CheckedAbs<T extends GenNumber> implements TripleExpression<T> {
    final private TripleExpression<T> expression;

    public CheckedAbs(TripleExpression<T> expression) {
        if (expression == null) {
            throw new NullPoTerException();
        }
        this.expression = expression;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        T res = expression.evaluate(x, y, z);
        if (res.compare(new T(0)) >= 0) {
            return res;
        }
        if (res == Integer.MIN_VALUE)
            throw new EvaluateException("Abs from INT_MIN attempt.");
        return -res;
    }
}
