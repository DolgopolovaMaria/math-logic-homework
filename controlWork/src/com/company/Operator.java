package com.company;

public class Operator implements Formula {

    public OperationType operationType;
    public Formula left;
    public Formula right;

    public Operator(OperationType type, Formula left, Formula right) {
        this.operationType = type;
        this.left = left;
        this.right = right;
    }

    public Operator(Formula right) {
        this.operationType = OperationType.Negation;
        this.right = right;
    }

    public String print() {
        switch (operationType) {
            case Negation:
                return "(-" + right.print() + ")";
            case Conjunction:
                return "(" + left.print() + " & " + right.print() + ")";
            case Disjunction:
                return "(" + left.print() + " | " + right.print() + ")";
            case Implication:
                return "(" + left.print() + " > " + right.print() + ")";
            default:
                return "";
        }
    }
}
