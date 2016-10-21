package expression.exceptions;

/**
 * Created by aydar on 14.03.16.
 */
public class CheckedAdd extends BinaryOperator {
    public CheckedAdd(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int action(int left, int right) {
        if ((left > 0 && right > 0 && (left - Integer.MAX_VALUE) + right > 0) ||
                (left < 0 && right < 0 && (left - Integer.MIN_VALUE) + right < 0)) {
            throw new EvaluateException("Adding overflow.");
        }
        return left + right;
    }
}
