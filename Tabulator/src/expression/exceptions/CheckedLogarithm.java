package expression.exceptions;

import java.util.LinkedList;

/**
 * Created by aydar on 11.04.16.
 */
public class CheckedLogarithm extends BinaryOperator {
    public CheckedLogarithm(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    @Override
    protected int action(int left, int right) {
        if (left <= 0 || right <= 1) {
            throw new EvaluateException("Not positive argument or not positive or 1 base recieved by logarithm.");
        }
        LinkedList<Integer> powers = new LinkedList<>();
        int i = 1;
        for (int p = right; p <= left; p *= p) {
            powers.addFirst(p);
            i *= 2;
            if ((p * p) / p != p)
                break;
        }
        i /= 2;
        int res = 0;
        int match = 1;
        while (!powers.isEmpty()) {
            if (left / match >= powers.peekFirst()) {
                res += i;
                match *= powers.peekFirst();
            }
            powers.removeFirst();
            i /= 2;
        }
        return res;
    }
}
