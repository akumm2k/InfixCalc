package calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Operations supported:
 *  summing, subtracting, multiplying, and dividing
 * --
 * Default type: double
 */
public class Calc {
    private final static Map<String, Double> VAR_TO_VAL = new HashMap<>();
    public final static String LAST_VAR = "last";
    static {
        VAR_TO_VAL.put(LAST_VAR, 0d);
    }

    /**
     * Calc is a utility class and does not support an object construction
     */
    public Calc() {
        throw new UnsupportedOperationException("Main Class - not supposed to be instantiated");
    }

    /**
     * Allows Assignment Operation to store the variable without have free access to the storage
     * @param var variable name
     * @param val value of that variable
     */
    public static void addVar(final String var, final double val) {
        VAR_TO_VAL.put(var, val);
    }

    /**
     * Allows Assignment Operation to access the variable without have free access to the storage
     * @param var variable name
     * @return the value of that variable
     */
    public static double getVarVal(final String var) {
        return VAR_TO_VAL.getOrDefault(var, 0d);
    }

    public static void main(String[] args) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            String expr;
            double result;

            System.out.print(">> ");
            while ((expr = input.readLine()) != null) {
                if (Pattern.matches("\\s*", expr)) { continue; }

                result = 0d;
                try {
                    final ExprParser parser = new ExprParser(expr);
                    BaseExpression root = parser.getParsedExpr();
                    result = root.eval();

                    System.out.printf("%.5f%n", result);
                } catch (StackOverflowError | ExprParser.SyntaxError e) {
                    System.out.println("ERROR");
                }
                VAR_TO_VAL.put(LAST_VAR, result);
                System.out.print(">> ");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
