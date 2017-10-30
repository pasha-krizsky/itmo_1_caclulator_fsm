package logic;

import model.Context;
import model.FSM;
import model.Operator;

import java.util.LinkedList;

/** Contains parsing logic and encapsulates FSM */
public class Parser {

    /** Parses expression */
    public static Context parse(String expr) {

        // Initial state
        FSM currentState = FSM.START;
        // Initial context
        Context context = new Context(expr);

        while (!context.getRestExpression().isEmpty()) {

            // Get another character
            Character c = context.getRestExpression().charAt(0);
            // Change state
            currentState = jump(c, currentState);

            // Switch based on current state
            switch (currentState) {
                case NUMBER:
                case U_OPERATOR:
                    num(context);
                    break;
                case B_OPERATOR:
                    op(context);
                    break;
                case L_BRACKET:
                    leftBracket(context);
                    break;
                case R_BRACKET:
                    rightBracket(context);
                    break;
                case SPACE:
                    space(context);
                    break;
            }
        }

        if (context.getNums().get(0).size() != 0) {
            Calculator.calc(context);
        }

        return context;
    }

    /** Jumps from current state to another state if possible */
    static FSM jump(Character c, FSM currentState) {

        if (isNum(c)) {
            return jumpNum(currentState);
        } else if (isBOperator(c)) {
            return jumpBOperator(currentState);
        } else if (isLeftBracket(c)) {
            return jumpLeftBracket(currentState);
        } else if (isRightBracket(c)) {
            return jumpRightBracket(currentState);
        } else if (isSpace(c)) {
            return jumpSpace(currentState);
        }

        throw new IllegalArgumentException("Cannot convert states");
    }

    /** Jumps from current state to NUMBER if possible */
    private static FSM jumpNum(FSM currentState) {

        switch (currentState) {
            case NUMBER:
            case SPACE:
            case L_BRACKET:
            case B_OPERATOR:
            case U_OPERATOR:
            case START:
                return FSM.NUMBER;
            default:
                throw new IllegalArgumentException("Cannot jump from state " + currentState.toString()
                        + " to state " + FSM.NUMBER.toString());
        }
    }

    /** Jumps from current state to B_OPERATOR if possible */
    private static FSM jumpBOperator(FSM currentState) {

        switch (currentState) {
            case R_BRACKET:
            case NUMBER:
            case SPACE:
            case U_OPERATOR:
                return FSM.B_OPERATOR;
            default:
                return jumpUOperator(currentState);
        }
    }

    /** Jumps from current state to U_OPERATOR if possible */
    private static FSM jumpUOperator(FSM currentState) {

        switch (currentState) {
            case START:
            case SPACE:
            case L_BRACKET:
            case B_OPERATOR:
                return FSM.U_OPERATOR;
            default:
                throw new IllegalArgumentException("Cannot jump from state " + currentState.toString()
                        + " to state " + FSM.U_OPERATOR.toString());
        }
    }

    /** Jumps from current state to L_BRACKET if possible */
    private static FSM jumpLeftBracket(FSM currentState) {

        switch (currentState) {
            case START:
            case SPACE:
            case B_OPERATOR:
            case L_BRACKET:
                return FSM.L_BRACKET;
            default:
                throw new IllegalArgumentException("Cannot jump from state " + currentState.toString()
                        + " to state " + FSM.L_BRACKET.toString());
        }
    }

    /** Jumps from current state to R_BRACKET if possible */
    private static FSM jumpRightBracket(FSM currentState) {

        switch (currentState) {
            case R_BRACKET:
            case NUMBER:
            case SPACE:
                return FSM.R_BRACKET;
            default:
                throw new IllegalArgumentException("Cannot jump from state " + currentState.toString()
                        + " to state " + FSM.R_BRACKET.toString());
        }
    }

    /** Jumps from current state to SPACE if possible */
    private static FSM jumpSpace(FSM currentState) {

        switch (currentState) {
            case START:
            case SPACE:
            case L_BRACKET:
            case R_BRACKET:
            case NUMBER:
            case B_OPERATOR:
            case U_OPERATOR:
                return FSM.SPACE;
            default:
                throw new IllegalArgumentException("Cannot jump from state " + currentState.toString()
                        + " to state " + FSM.SPACE.toString());
        }
    }

    /** Extracts first num from expression */
    static void num(Context res) {

        int currentPos = 0;
        int countPoints = 0;
        boolean negative = false;
        String expr = res.getRestExpression();

        if (expr.charAt(currentPos) == '-') {
            negative = true;
            expr = expr.substring(1);
        }

        while (currentPos < expr.length()
                && (Character.isDigit(expr.charAt(currentPos)) || expr.charAt(currentPos) == '.')) {
            if (expr.charAt(currentPos) == '.' && ++countPoints > 1) {
                throw new IllegalArgumentException("To many points in the number: " + expr.substring(0, currentPos+1));
            }
            ++currentPos;
        }

        if (currentPos == 0) {
            throw new IllegalArgumentException("Number not found: " + expr.charAt(0));
        }

        double currentResult = Double.parseDouble(expr.substring(0, currentPos));
        if (negative) {
            currentResult = -currentResult;
        }

        res.setRestExpression(expr.substring(currentPos));
        res.getNums().get(res.getNums().size()-1).add(currentResult);
    }

    /** Extracts operator (first symbol) from expr */
    static void op(Context res) {

        String expr = res.getRestExpression();
        Character op = expr.charAt(0);
        res.setRestExpression(expr.substring(1));
        res.getOps().get(res.getOps().size()-1).add(op);
    }

    /** Increments position of operators and operands */
    static void leftBracket(Context res) {
        String expr = res.getRestExpression();
        res.setRestExpression(expr.substring(1));
        res.getOps().add(new LinkedList<>());
        res.getNums().add(new LinkedList<>());
    }

    /** Extracts right bracket and performs calculation of expression in brackets */
    static void rightBracket(Context res) {
        Calculator.calc(res);
        String expr = res.getRestExpression();
        res.setRestExpression(expr.substring(1));
        res.getOps().remove(res.getOps().size()-1);
        res.getNums().remove(res.getNums().size()-1);
    }

    /** Skips space */
    static void space(Context res) {
        String expr = res.getRestExpression();
        res.setRestExpression(expr.substring(1));
    }

    /** Checks if character is a part of number */
    private static boolean isNum(Character c) {
        return Character.isDigit(c);
    }

    /** Checks if character is an operator */
    private static boolean isBOperator(Character c) {
        return  c.equals(Operator.ADD.getValue()) ||
                c.equals(Operator.SUBTRACT.getValue()) ||
                c.equals(Operator.MULTIPLY.getValue()) ||
                c.equals(Operator.DIVIDE.getValue());
    }

    /** Checks if character is an unary operator */
    private static boolean isUOperator(Character c) {
        return c.equals(Operator.SUBTRACT.getValue());
    }

    /** Checks if character is a left bracket */
    private static boolean isLeftBracket(Character c) {
        return c.equals('(');
    }

    /** Checks if character is a right bracket */
    private static boolean isRightBracket(Character c) {
        return c.equals(')');
    }

    /** Checks if character is a space */
    private static boolean isSpace(Character c) {
        return c.equals(' ') || c.equals('\t');
    }
}
