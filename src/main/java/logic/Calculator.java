package logic;

import model.Operator;
import model.Context;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** Calculator, performs calculation based on given set of operands and operators */
public class Calculator {

    /** Calculates expression in brackets */
    public static void calc(Context res) {

        // Extract numbers and operators from context
        List<Double> nums = new LinkedList<>(res.getNums().get(res.getNums().size()-1));
        List<Character> ops = new LinkedList<>(res.getOps().get(res.getOps().size()-1));

        if (!(nums.size() < 2)) {
            double result = calc(nums, ops);
            res.setResult(result);
            if (res.getNums().size() > 1) {
                res.getNums().get(res.getNums().size()-2).add(result);
            }
        }
    }

    /** Calculates expression based on given list of numbers and operators */
    public static double calc(List<Double> nums, List<Character> ops) {

        multiplyAndDivide(nums, ops);
        addAndSubtract(nums, ops);

        return nums.get(0);
    }

    private static void multiplyAndDivide(List<Double> nums, List<Character> ops) {

        Iterator<Character> it = ops.iterator();
        int i = 0;
        while (it.hasNext()) {
            Character c = it.next();
            if (c.equals(Operator.MULTIPLY.getValue())) {
                nums.set(i, nums.get(i) * nums.get(i+1));
                nums.remove(i+1);
                it.remove();
                --i;
            } else if (c.equals(Operator.DIVIDE.getValue())) {
                if (nums.get(i+1).equals(0.)) {
                    throw new IllegalArgumentException("Cannot divide by zero: " + nums.get(i) + "/" + nums.get(i+1));
                }
                nums.set(i, nums.get(i) / nums.get(i+1));
                nums.remove(i+1);
                it.remove();
                --i;
            }
            ++i;
        }
    }

    private static void addAndSubtract(List<Double> nums, List<Character> ops) {

        Iterator<Character> it = ops.iterator();
        int i = 0;
        while (it.hasNext()) {
            Character c = it.next();
            if (c.equals(Operator.ADD.getValue())) {
                nums.set(i, nums.get(i) + nums.get(i+1));
                nums.remove(i+1);
                it.remove();
                --i;
            } else if (c.equals(Operator.SUBTRACT.getValue())) {
                nums.set(i, nums.get(i) - nums.get(i+1));
                nums.remove(i+1);
                it.remove();
                --i;
            }
            ++i;
        }
    }
}
