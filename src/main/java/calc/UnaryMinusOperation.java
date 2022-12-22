package calc;

public class UnaryMinusOperation implements BaseExpression {
    private final BaseExpression expr;

    public UnaryMinusOperation(BaseExpression expr) {
        this.expr = expr;
    }

    @Override
    public double eval() {
        return -expr.eval();
    }
}
