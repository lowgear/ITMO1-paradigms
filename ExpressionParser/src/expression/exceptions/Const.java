package expression.exceptions;

/**
 * Created by aydar on 04.04.16.
 */
public class Const implements TripleExpression {
    final private int value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }
}
