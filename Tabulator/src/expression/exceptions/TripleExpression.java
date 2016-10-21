package expression.exceptions;

import expression.generic.GenNumber;

/**
 * Created by aydar on 04.04.16.
 */
public interface TripleExpression<T extends GenNumber> {
    T evaluate(T x, T y, T z);
}
