package calc;

public class BinaryOperation implements BaseExpression {
    private final String opr;
    private final BaseExpression left;
    private final BaseExpression right;

    /**
     *
     * @param opr the operator string representing the operation
     * @param left the left operand expression
     * @param right the right operand expression
     */
    public BinaryOperation(String opr, BaseExpression left, BaseExpression right) {
        this.opr = opr;
        this.left = left;
        this.right = right;
    }

    /**
     * Evaluate each operand individually and then apply the binary operation
     * @return the evaluated result of the binary operation
     */
    @Override
    public double eval() {
        final double leftVal = this.left.eval();
        final double rightVal = this.right.eval();
        return switch (opr) {
            case ExprSyntax.PLUS -> leftVal + rightVal;
            case ExprSyntax.MINUS -> leftVal - rightVal;
            case ExprSyntax.MUL -> leftVal * rightVal;
            case ExprSyntax.DIV -> leftVal / rightVal; // purposefully don't handle div by 0 error

            default -> throw new RuntimeException("Bad operation");
        };
    }
}
