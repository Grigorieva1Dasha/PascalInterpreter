package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Parser {
    private Token current;
    private Lexer lexer;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        current = this.lexer.nextToken();
    }

    public void checkTokenType(TokenType type) throws Exception {
        if (current.getType() == type) {
            current = lexer.nextToken();
        } else {
            throw new Exception("Token error");
        }
    }

    private Node factor() throws Exception {
        Token token = current;
        if (token.getType().equals(TokenType.MINUS)) {
            checkTokenType(TokenType.MINUS);
            return new UnOp(token, factor());
        } else if (token.getType().equals(TokenType.PLUS)) {
            checkTokenType(TokenType.PLUS);
            return new UnOp(token, factor());
        } else if (token.getType().equals(TokenType.INTEGER)) {
            checkTokenType(TokenType.INTEGER);
            return new Number(token);
        } else if (token.getType().equals(TokenType.LPAREN)) {
            checkTokenType(TokenType.LPAREN);
            Node result = expr();
            checkTokenType(TokenType.RPAREN);
            return result;
        } else if (token.getType().equals(TokenType.VAR)) {
            checkTokenType(TokenType.VAR);
            return new Var(token);
        }
        throw new Exception("Factor error");
    }

    private Node term() throws Exception {
        Node result = factor();
        List<TokenType> ops = Arrays.asList(TokenType.DIV, TokenType.MUL);
        while (ops.contains(current.getType())) {
            Token token = current;
            if (token.getType().equals(TokenType.DIV)) {
                checkTokenType(TokenType.DIV);
            } else if (token.getType().equals(TokenType.MUL)) {
                checkTokenType(TokenType.MUL);
            }
            result = new BinOp(result, token, factor());
        }
        return result;
    }

    public Node expr() throws Exception {
        List<TokenType> ops = Arrays.asList(TokenType.PLUS, TokenType.MINUS);
        Node result = term();
        while (ops.contains(current.getType())) {
            Token token = current;
            if (token.getType().equals(TokenType.PLUS)) {
                checkTokenType(TokenType.PLUS);
            } else if (token.getType().equals(TokenType.MINUS)) {
                checkTokenType(TokenType.MINUS);
            }
            result = new BinOp(result, token, term());
        }
        return result;
    }

    public Node parse() throws Exception {
        return program();
    }

    public Node assignment() throws Exception {
        Node var = new Var(current);
        checkTokenType(TokenType.VAR);
        checkTokenType(TokenType.ASSIGN);
        Node expr = expr();
        return new Assign(var, expr);
    }

    private Node statement() throws Exception {
        if (current.getType().equals(TokenType.BEGIN)){
            return complexstatement();
        }
        else if (current.getType().equals(TokenType.VAR)){
            return assignment();
        }
        else return new Empty();
    }

    private Node complexstatement() throws Exception{
        checkTokenType(TokenType.BEGIN);
        List<Node> statements = statementlist();
        checkTokenType(TokenType.END);
        return new Complexstatement(statements);
    }

    private List<Node> statementlist() throws Exception{
        Node statement = statement();
        List<Node> res = new ArrayList<>();
        res.add(statement);
        while (current.getType().equals(TokenType.SEMI)){
            checkTokenType(TokenType.SEMI);
            res.add(statement());
        }
        if (current.getType().equals(TokenType.VAR)){
            throw new Exception("Var error!");
        }
        return res;
    }

    private Node program() throws Exception {
        Node node = complexstatement();
        checkTokenType(TokenType.DOT);
        return node;
    }

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("2 + (2 * 3) - 4");
        Parser parser = new Parser(lexer);
        System.out.println(parser.parse());
    }
}
