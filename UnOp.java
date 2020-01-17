package com.company;

public class UnOp extends Node {
    private Token op;
    private Node right;

    public UnOp(Token op, Node right) {
        this.op = op;
        this.right = right;
    }

    public Token getOp() {
        return op;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public String toString() {
        return String.format("UnOp%s (%s)", op.getValue(), right);
    }
}
