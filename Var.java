package com.company;

public class Var extends Node {
    private Token token;

    public Var(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Var{" +
                "token=" + token +
                '}';
    }
}
