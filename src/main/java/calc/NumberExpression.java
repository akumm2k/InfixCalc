package calc;

/**
 * Expression wrapper for doubles
 */
public final class NumberExpression implements BaseExpression {
    private final double num;

    public NumberExpression(double num) {
        this.num = num;
    }

    @Override
    public double eval() {
        return num;
    }
}
