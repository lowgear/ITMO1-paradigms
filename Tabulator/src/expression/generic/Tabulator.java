package expression.generic;

/**
 * Created by aydar on 19.04.16.
 */
public interface Tabulator {
    Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception;
}
