package model;

/** Finite state machine for calculator parser */
public enum FSM {
    START,
    NUMBER,
    U_OPERATOR,
    B_OPERATOR,
    L_BRACKET,
    R_BRACKET,
    SPACE
}
