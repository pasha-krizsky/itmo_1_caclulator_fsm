package model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/** Current context of calculations */
public class Context {

    @Getter
    @Setter
    private String restExpression;

    @Getter
    private List<List<Double>> nums;

    @Getter
    private List<List<Character>> ops;

    @Getter
    @Setter
    private double result;

    public Context(String expression) {
        ops = new LinkedList<>();
        ops.add(new LinkedList<>());
        nums = new LinkedList<>();
        nums.add(new LinkedList<>());
        this.restExpression = expression;
    }
}
