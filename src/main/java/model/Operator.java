package model;

import lombok.Getter;

/** Possible operators in expression */
public enum Operator {

    ADD('+'),
    SUBTRACT('-'),
    MULTIPLY('*'),
    DIVIDE('/');

    @Getter
    private Character value;

    Operator(Character value) {
        this.value = value;
    }
}
