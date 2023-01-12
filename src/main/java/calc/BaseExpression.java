package calc;

public sealed interface BaseExpression permits
        AssignmentOperation,
        BinaryOperation, UnaryMinusOperation,
        NumberExpression, VariableExpression {
    /**
     * All implementations of the interface should be evaluated to a double
     * @return the evaluated value
     */
    double eval();
}
