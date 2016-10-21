import expression.*;

/**
 * Created by aydar on 14.03.16.
 */
public class Main {
    public static void main(String[] args) {
        double d = Double.parseDouble(args[0]);
        d = new Add(
                new Subtract(
                        new Multiply(
                                new Variable("x"),
                                new Variable("x")),
                        new Multiply(
                                new Const(2.0),
                                new Variable("x"))),
                new Const(1.0)
        ).evaluate(d);
        System.out.println(d);
        int i = Integer.parseInt(args[0]);
        i = new Add(
                new Subtract(
                        new Multiply(
                                new Variable("x"),
                                new Variable("x")),
                        new Multiply(
                                new Const(2),
                                new Variable("x"))),
                new Const(1)
        ).evaluate(i);
        System.out.println(i);
    }
}
//x2−2x+1