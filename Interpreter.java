package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements NodeVisitor {
    private Parser parser;
    private Map<String, Float> vars;

    public Interpreter(Parser parser) throws Exception {
        this.parser = parser;
        this.vars = new HashMap<>();
    }

    @Override
    public float visit(Node node) throws Exception {
        if (node.getClass().equals(BinOp.class)) {
            return visitBinOp(node);
        } else if (node.getClass().equals(Number.class)) {
            return visitNumber(node);
        } else if (node.getClass().equals(UnOp.class)) {
            return visitUnOp(node);
        } else if (node instanceof Assign) {
            return visitAssign(node);
        } else if (node instanceof Var) {
            return visitVar(node);
        } else if (node instanceof Complexstatement) {
            return visitComSt(node);
        } else if (node instanceof Empty) {
            return 0;
        } else throw new Exception("Interpreter error" + node.getClass().getSimpleName());
    }

    public float visitComSt(Node node) throws Exception {
        List<Node> children = ((Complexstatement) node).getStatements();
        for (Node n : children) {
            visit(n);
        }
        return 0;
    }

    public float visitAssign(Node node) throws Exception {
        Assign assign = (Assign) node;
        String varName = ((Var) assign.getLeft()).getToken().getValue();
        vars.put(varName, visit(assign.getRight()));
        return 0;
    }

    public float visitVar(Node node) throws Exception {
        Var var = (Var) node;
        return vars.get(var.getToken().getValue());
    }

    public float visitBinOp(Node node) throws Exception {
        BinOp binOp = (BinOp) node;
        if (binOp.getOp().getType().equals(TokenType.PLUS)) {
            return visit(binOp.getLeft()) + visit(binOp.getRight());
        } else if (binOp.getOp().getType().equals(TokenType.MINUS)) {
            return visit(binOp.getLeft()) - visit(binOp.getRight());
        } else if (binOp.getOp().getType().equals(TokenType.DIV)) {
            return visit(binOp.getLeft()) / visit(binOp.getRight());
        } else if (binOp.getOp().getType().equals(TokenType.MUL)) {
            return visit(binOp.getLeft()) * visit(binOp.getRight());
        }

        throw new Exception("BinOp Interpreter error");
    }

    public float visitUnOp(Node node) throws Exception {
        UnOp unop = (UnOp) node;
        if (unop.getOp().getType().equals(TokenType.MINUS)) {
            return -visit(unop.getRight());
        } else if (unop.getOp().getType().equals(TokenType.PLUS)) {
            return visit(unop.getRight());
        }
        throw new Exception("UnOp Interpreter error");
    }

    public float visitNumber(Node node) {
        Number number = (Number) node;
        return Float.parseFloat(number.getToken().getValue());
    }

    public float interpret() throws Exception {
        Node tree = parser.parse();
        return visit(tree);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("in> ");
            String text = reader.readLine();

            if (text.equals("exit") | text.length() < 1) {
                break;
            }

            Interpreter interp = new Interpreter(new Parser(new Lexer(text)));
            System.out.print("out> ");
            try {
                float result = interp.interpret();
                System.out.println(interp.vars);
//                System.out.println(result);
            } catch (Exception e) {
                System.out.println("E:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
