package expression.exceptions;

/**
 * Created by aydar on 04.04.16.
 */
public interface Parser<T extends GenNumber> {
    TripleExpression<T> parse(String expression);
}
