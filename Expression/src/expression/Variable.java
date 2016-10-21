package expression;

/**
 * Created by aydar on 14.03.16.
 */
public class Variable implements ComboExpression {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "x": return x;
            case "y": return y;
            case "z": return z;
            default: return 0;
        }
    }
}
