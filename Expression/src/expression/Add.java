package expression;

/**
 * Created by aydar on 14.03.16.
 */
public class Add extends BinaryOperator {
    public Add(ComboExpression left, ComboExpression right) {
        super(left, right);
    }

    @Override
    protected int action(int left, int right) {
        return left + right;
    }

    @Override
    protected double action(double left, double right) {
        return left + right;
    }
}
