package expression.exceptions;

import expression.generic.GenNumber;

/**
 * Created by aydar on 04.04.16.
 */
public class Const<T extends GenNumber> implements TripleExpression<T> {
    final private T value;

    public Const(T value) {
        this.value = value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }
}
