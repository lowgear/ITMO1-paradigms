package expression;

/**
 * Created by aydar on 14.03.16.
 */
abstract class BinaryOperator implements ComboExpression {
    final ComboExpression left, right;

    public BinaryOperator(ComboExpression left, ComboExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate(int x) {
        return action(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public double evaluate(double x) {
        return action(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return action(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    protected abstract int action(int left, int right);

    protected abstract double action(double left, double right);
}
