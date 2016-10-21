package expression;

/**
 * Created by aydar on 14.03.16.
 */
public class Const implements ComboExpression {
    final private double value;

    public Const(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(double x) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return (int) value;
    }

    @Override
    public int evaluate(int x) {
        return (int) value;
    }
}
