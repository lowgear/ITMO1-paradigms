package expression.exceptions;

import expression.generic.GenNumber;

/**
 * Created by aydar on 04.04.16.
 */
abstract class BinaryOperator<T extends GenNumber> implements TripleExpression<T> {
    private final TripleExpression<T> left, right;

    public BinaryOperator(TripleExpression<T> left, TripleExpression<T> right) {
        if (left == null || right == null) {
            throw new NullPointerException("Null pointer given to constructor.");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return action(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    protected abstract T action(T left, T right);
}