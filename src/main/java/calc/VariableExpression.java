package calc;

public class VariableExpression implements BaseExpression {
    private final String var;

    public VariableExpression(String var) {
        this.var = var;
    }

    /**
     *
     * @return the value of the variable stored in the Calculator
     */
    @Override
    public double eval() {
        // purposefully don't check if key exists
        return Calc.getVarVal(this.var);
    }
}