package com.company;

public class Assign extends Node {
    private Node left;
    private Node right;

    public Assign(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Assign{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
