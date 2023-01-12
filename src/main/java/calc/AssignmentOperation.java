package calc;

public final class AssignmentOperation implements BaseExpression {

    private final String var;
    private final BaseExpression rhs;

    /**
     * Assign an expression to the variable
     * @param var the variable assigned to
     * @param rhs the right hand side expression of the assignment
     */
    public AssignmentOperation(String var, BaseExpression rhs) {
        this.var = var;
        this.rhs = rhs;
    }

    /**
     * Store the evaluated value of the right hand side expression into the calculator's map
     * @return the evaluated value of the right hand side expression
     */
    @Override
    public double eval() {
        double val = this.rhs.eval();
        Calc.addVar(this.var, val);
        return val;
    }
}
