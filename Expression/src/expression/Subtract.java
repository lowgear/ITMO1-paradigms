package expression;

/**
 * Created by aydar on 14.03.16.
 */
public class Subtract extends BinaryOperator {
    public Subtract(ComboExpression left, ComboExpression right) {
        super(left, right);
    }

    @Override
    protected int action(int left, int right) {
        return left - right;
    }

    @Override
    protected double action(double left, double right) {
        return left - right;
    }
}
