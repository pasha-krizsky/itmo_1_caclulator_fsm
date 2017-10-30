package logic;

import model.Context;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CalculatorTest {

    private final double e = 0.000001;

    @Test
    public void parsePositive() {

        Map<String, Double> expressionAndResult = new HashMap<>();

        expressionAndResult.put("3+3", 6.);
        expressionAndResult.put("3-3", 0.);
        expressionAndResult.put("3*3", 9.);
        expressionAndResult.put("3/3", 1.);
        expressionAndResult.put("-3+3", 0.);
        expressionAndResult.put("3+-3", 0.);
        expressionAndResult.put("-3+-3", -6.);
        expressionAndResult.put("2+2*(3-1+5)", 16.);
        expressionAndResult.put("(2+2*(3-1+5))", 16.);
        expressionAndResult.put(" (    2 +  2* (3 -1+5))", 16.);
        expressionAndResult.put("1.0+2-(100*32-11.)-4-5-(1000-33*(2-1*(1+3)))", -4261.);

        for (Map.Entry<String, Double> pair: expressionAndResult.entrySet()) {
            double expected = pair.getValue();
            Context actual = Parser.parse(pair.getKey());
            assertEquals(expected, actual.getResult(), e);
        }
    }

    @Test
    public void parseNegative() {

        Map<String, Double> expressionAndResult = new HashMap<>();

        expressionAndResult.put("3/0", 0.0);
        expressionAndResult.put("3++", 0.0);
        expressionAndResult.put("3..", 0.0);
        expressionAndResult.put("3+)", 0.0);
        expressionAndResult.put("(1+2)3", 0.0);
        expressionAndResult.put("hello", 0.0);

        for (Map.Entry<String, Double> pair: expressionAndResult.entrySet()) {
            try {
                Parser.parse(pair.getKey());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @Test
    public void leftBracket() {
        String expr = "(1+2+3)";
        String expected = "1+2+3)";
        Context context = new Context(expr);
        Parser.leftBracket(context);
        assertEquals(expected, context.getRestExpression());
        assertEquals(2, context.getOps().size());
        assertEquals(2, context.getNums().size());
    }

    @Test
    public void op() {
        String expr = "+2-1";
        Character expected = '+';
        Context context = new Context(expr);
        Parser.op(context);
        assertEquals(expected, context.getOps().get(0).get(0));
    }

    @Test
    public void num() {
        String expr = "1+2-3*4/5";
        String expr2 = "-11-12";
        double expected = 1.0;
        double expected2 = -11.0;
        String expectedRest = "+2-3*4/5";
        String expectedRest2 = "-12";
        Context context = new Context(expr);
        Parser.num(context);
        Context context2 = new Context(expr2);
        Parser.num(context2);

        assertEquals(expected, context.getNums().get(0).get(0), e);
        assertEquals(expected2, context2.getNums().get(0).get(0), e);
        assertEquals(expectedRest, context.getRestExpression());
        assertEquals(expectedRest2, context2.getRestExpression());
    }
}
