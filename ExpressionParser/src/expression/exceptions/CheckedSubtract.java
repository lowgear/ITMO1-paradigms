package expression.exceptions;

/**
 * Created by aydar on 14.03.16.
 */
public class CheckedSubtract extends BinaryOperator {
    public CheckedSubtract(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int action(int left, int right) {
        if ((left < 0 && right > 0 && (left - Integer.MIN_VALUE) - right < 0) ||
                (left >= 0 && right < 0 && (left + Integer.MIN_VALUE) - right >= 0)) {
            throw new EvaluateException("Substract overflow.");
        }
        return left - right;
    }
}
