package expression.exceptions;

/**
 * Created by aydar on 14.03.16.
 */
public class CheckedDivide extends BinaryOperator {
    public CheckedDivide(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int action(int left, int right) {
        if (right == 0) {
            throw new EvaluateException("Zero devisor recieved.");
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new EvaluateException("MIN_INT divided by -1.");
        }
        return left / right;
    }
}
