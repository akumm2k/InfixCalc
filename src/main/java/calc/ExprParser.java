package calc;

import java.util.Collections;
import java.util.List;

/**
 * A recursive descent parser for the following CFG.
 * <br/> E -> T | E '+' T | E '-' T
 * <br/> T -> F | T '*' F | T '/' F
 * <br/> F -> P | '-'P
 * <br/> P -> E | '('E')' | V | N
 */
public class ExprParser {
    private final List<String> tokens;
    public static class SyntaxError extends Error {
        public SyntaxError(String message) {
            super(message);
        }

    }

    /**
     * Initialize a parser with the expression to be parsed
     * @param expr the expression string
     */
    public ExprParser(final String expr) {
        this.tokens = Collections.unmodifiableList(
            ExprSyntax.tokenize(expr)
        );
    }

    /**
     * parse the expression provided to the ExprParser
     * @return the root of the parse tree
     */
    public BaseExpression getParsedExpr() {
        if (this.tokens.size() > 1 && this.tokens.get(1).equals("=")) {
            final String var = this.tokens.get(0);
            return new AssignmentOperation(var, exprParse(2).expr());
        }

        ExprTokenIdPair exprPair = exprParse(0);
        if (exprPair.tokenIndex() != this.tokens.size()) {
            throw new SyntaxError("bad expr");
        }
        return exprPair.expr();
    }

    private record ExprTokenIdPair(BaseExpression expr, int tokenIndex){ }

    private ExprTokenIdPair primaryParse(final int tokenIndex) {
        if (tokenIndex >= this.tokens.size()) {
            return exprParse(tokenIndex);
        }

        String token = this.tokens.get(tokenIndex);
        if (token.equals(ExprSyntax.OPEN_PAREN)) {
            ExprTokenIdPair exprTokenIdPair = exprParse(tokenIndex + 1);
            if (!this.tokens.get(exprTokenIdPair.tokenIndex()).equals(ExprSyntax.CLOSE_PAREN)) {
                throw new SyntaxError("Where did the close paren go?");
            }
            // skip close paren
            return new ExprTokenIdPair(exprTokenIdPair.expr(), exprTokenIdPair.tokenIndex() + 1);
        } else if (ExprSyntax.isNum(token)) {

            final double dub = Double.parseDouble(this.tokens.get(tokenIndex));
            return new ExprTokenIdPair(new NumberExpression(dub), tokenIndex + 1);

        } else if (ExprSyntax.isVar(token)) {
            return new ExprTokenIdPair(new VariableExpression(token), tokenIndex + 1);
        }

        return exprParse(tokenIndex);
    }

    private ExprTokenIdPair factorParse(final int tokenIndex) {
        if (tokenIndex < this.tokens.size() && this.tokens.get(tokenIndex).equals(ExprSyntax.MINUS)) {
            final ExprTokenIdPair primaryPair = primaryParse(tokenIndex + 1);

            return new ExprTokenIdPair(
              new UnaryMinusOperation(primaryPair.expr),
              primaryPair.tokenIndex()
            );

        }
        return primaryParse(tokenIndex);
    }

    private ExprTokenIdPair termParse(final int tokenIndex) {
        ExprTokenIdPair factorPair = factorParse(tokenIndex);

        while (true) {
            if (factorPair.tokenIndex() >= this.tokens.size()) {
                return factorPair;
            }
            final String nextToken = this.tokens.get(factorPair.tokenIndex());

            switch (nextToken) {
                case ExprSyntax.MUL -> {
                    final ExprTokenIdPair secondOperandPair = factorParse(
                            factorPair.tokenIndex() + 1
                    );
                    factorPair = createBinaryPair(ExprSyntax.MUL, factorPair.expr(), secondOperandPair);
                }
                case ExprSyntax.DIV -> {
                    final ExprTokenIdPair secondOperandPair = factorParse(
                            factorPair.tokenIndex() + 1
                    );
                    factorPair = createBinaryPair(ExprSyntax.DIV, factorPair.expr(), secondOperandPair);
                }
                case ExprSyntax.OPEN_PAREN -> throw new SyntaxError("bad paren");

                default -> {
                    return factorPair;
                }
            }
        }
    }

    private ExprTokenIdPair createBinaryPair(final String Operator,
                                             final BaseExpression leftOperand,
                                             final ExprTokenIdPair rightOperandPair) {
        return new ExprTokenIdPair(
                new BinaryOperation(
                        Operator, leftOperand, rightOperandPair.expr()
                ),
                rightOperandPair.tokenIndex()
        );
    }

    private ExprTokenIdPair exprParse(final int tokenIndex) {
        ExprTokenIdPair termPair = termParse(tokenIndex);
        while (true) {
            if (termPair.tokenIndex() == tokenIndex || termPair.tokenIndex() >= this.tokens.size()) {
                return termPair;
            }

            final String nextToken = this.tokens.get(termPair.tokenIndex());

            switch (nextToken) {
                case ExprSyntax.PLUS -> {
                    final ExprTokenIdPair secondOperandPair = termParse(
                            termPair.tokenIndex() + 1
                    );
                    termPair = createBinaryPair(ExprSyntax.PLUS, termPair.expr(), secondOperandPair);
                }
                case ExprSyntax.MINUS -> {
                    final ExprTokenIdPair secondOperandPair = termParse(
                            termPair.tokenIndex() + 1
                    );
                    termPair = createBinaryPair(ExprSyntax.MINUS, termPair.expr(), secondOperandPair);
                }
                case ExprSyntax.OPEN_PAREN -> throw new SyntaxError("bad paren");

                default -> {
                    return termPair;
                }
            }
        }
    }

}
