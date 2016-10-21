package expression.exceptions;

/**
 * Created by aydar on 04.04.16.
 */
abstract class BinaryOperator implements TripleExpression {
    private final TripleExpression left, right;

    public BinaryOperator(TripleExpression left, TripleExpression right) {
        if (left == null || right == null) {
            throw new NullPointerException("Null pointer given to constructor.");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return action(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    protected abstract int action(int left, int right);
}