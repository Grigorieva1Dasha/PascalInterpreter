package com.company;

import java.util.List;

public class Complexstatement extends Node {
    private List<Node> statements;

    public Complexstatement(List<Node> statements) {
        this.statements = statements;
    }

    public List<Node> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return "Complexstatement{" +
                "statements=" + statements +
                '}';
    }
}
