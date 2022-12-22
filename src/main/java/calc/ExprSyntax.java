package calc;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * The ExprSyntax class lays the syntactical rules and provides tools for verifying them in an expression
 */
public class ExprSyntax {
    public final static String PLUS = "+";
    public final static String MINUS = "-";
    public final static String MUL = "*";
    public final static String DIV = "/";
    public final static String ASS = "=";
    public final static String OPEN_PAREN = "(";
    public final static String CLOSE_PAREN = ")";


    private final static Pattern NUM_PATTERN = Pattern.compile("\\d+.*");
    private final static Pattern VAR_PATTERN = Pattern.compile("^[a-zA-Z].*");
    private final static Pattern OPR_PATTERN = Pattern.compile(operatorParenRegex());

    /**
     * ExprSyntax is a utility class, and does not support object construction
     */
    public ExprSyntax() {
        throw new UnsupportedOperationException("Utility Class - not supposed to be instantiated");
    }


    /**
     * if the operator is illegal, ie not defined in the ExprSyntax, the program throws up
     * @param opr the operator string
     * @return the precedence of the operator
     */
    public static int precedence(final String opr) {
        return switch (opr) {
            case MUL, DIV -> 2;
            case PLUS, MINUS -> 1;
            case ASS -> 0;
            default -> throw new RuntimeException("bad operator");
        };
    }


    /**
     *
     * @return a regex string that matches a valid operator or parenthesis
     */
    public static String operatorParenRegex() {
        final StringJoiner orRegJoiner = new StringJoiner("|");

        for (String opr: new String[] { PLUS, MINUS, MUL, DIV, ASS, OPEN_PAREN, CLOSE_PAREN } ) {
            final String quotedOpr = Pattern.quote(opr);
            orRegJoiner.add(quotedOpr);
        }
        return orRegJoiner.toString();
    }


    /**
     * Create a regex string to split an expression and preserve
     * the delimiting operators
     * @return a regex string to split expression, preserving the delimiting operators
     */
    @SuppressWarnings("unused")
    public static String forwardBackwardOperatorRegex() {
        return String.format("((?=%s))|((?<=%1$s))", operatorParenRegex());
    }


    @SuppressWarnings("unused")
    private static void tokenizeOpr(final List<String> tokens, final String oprChar) {
        // used for the unary trick
        if (oprChar.equals(ExprSyntax.MINUS) &&
                (tokens.size() == 0 || isOpr(tokens.get(tokens.size() - 1)))) {
            // treat unary minus as binary minus with left operand = 0
            tokens.add("0");
        }
        tokens.add(oprChar);
    }


    /**
     * The routine looks at the current token and returns true if the token is of the form \de(-|\d)
     * @param token the potentially scientific exponent notated token
     * @param currChar the current character being read during tokenization
     * @return true iff the token being built is a potential exponent-notated token
     */
    private static boolean potentiallyExponent(final StringBuilder token, final String currChar) {
        if (token.charAt(token.length() - 1) != 'e') {
            return false;
        }
        for (int i = 0; i < token.length() - 1; i++) {
            if (!isNum(String.valueOf(token.charAt(i)))) {
                return false;
            }
        }
        return currChar.equals(ExprSyntax.MINUS) || isNum(currChar);
    }

    /**
     * This routine tokenizes the non-operator characters as a number or a variable
     * @param tokens the tokens list
     * @param expr the expression string
     * @param nonOprChar the current character, provably not an operator, being read
     * @param exprItr The pointer / indexer to the current character being read
     * @return modified exprItr
     */
    private static int tokenizeNonOprAndGetItr(final List<String> tokens, final String expr,
                                               String nonOprChar, int exprItr) {
        final StringBuilder numVarToken = new StringBuilder();

        do {
            numVarToken.append(nonOprChar);
            exprItr++;
            if (exprItr < expr.length()) {
                nonOprChar = String.valueOf(expr.charAt(exprItr));
            }
        } while (exprItr < expr.length() &&
                !nonOprChar.equals(" ") &&
                (potentiallyExponent(numVarToken, nonOprChar) || !isOpr(nonOprChar)));

        tokens.add(numVarToken.toString());
        return exprItr;
    }


    /**
     *
     * @param expr the expression string
     * @return a list of tokens / strings, which we will parse and operate over.
     */
    public static List<String> tokenize(final String expr) {
        List<String> tokens = new ArrayList<>();
        int exprItr = 0;

        while (exprItr < expr.length()) {
            while (exprItr < expr.length() && expr.charAt(exprItr) == ' ') {
                exprItr++;
            }
            if (exprItr >= expr.length()) { break; }

            String exprChar = String.valueOf(expr.charAt(exprItr));
            if (isOpr(exprChar)) {
                // skips the unary trick for now for the recursive descent parser
                tokens.add(exprChar);
                exprItr++;
            } else {
                // it must be a number or variable type
                exprItr = tokenizeNonOprAndGetItr(tokens, expr, exprChar, exprItr);
            }
        }
        return tokens;
    }


    public static boolean isOpr(final String potentialOpr) {
        return OPR_PATTERN.matcher(potentialOpr).matches();
    }

    public static boolean isNum(final String potentialNum) {
        return NUM_PATTERN.matcher(potentialNum).matches();
    }

    public static boolean isVar(final String potentialVar) {
        return VAR_PATTERN.matcher(potentialVar).matches();
    }

    @Deprecated
    public static String rmWhiteSpace(final String expr) {
        // ! TODO: check if expressions like "1. 2 + 5" should be invalid
        StringJoiner stringJoiner = new StringJoiner("");
        for (String token: expr.split("\s+")) {
            stringJoiner.add(token);
        }
        return stringJoiner.toString();
    }
}
