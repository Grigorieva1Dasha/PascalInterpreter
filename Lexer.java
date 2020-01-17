package com.company;

public class Lexer {
    private String text;
    private int pos = -1;
    private Character currentChar;

    public Token nextToken() throws Exception {
        while (currentChar != null) {

            if (Character.isSpaceChar(currentChar)) {
                skip();
                continue;
            }

            if (Character.isDigit(currentChar)) {
                return new Token(TokenType.INTEGER, number());
            }

            if (Character.isAlphabetic(currentChar)){
                String varName = var();
                TokenType type;
                switch (varName) {
                    case "BEGIN": type = TokenType.BEGIN; break;
                    case  "END" : type = TokenType.END; break;
                    default: type = TokenType.VAR;
                }
                return new Token(type, varName);
            }

            if (currentChar == '+') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.PLUS, "" + temp);
            }

            if (currentChar == '-') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.MINUS, "" + temp);
            }

            if (currentChar == '*') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.MUL, "" + temp);
            }

            if (currentChar == '/') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.DIV, "" + temp);
            }

            if (currentChar == '(') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.LPAREN, "" + temp);
            }

            if (currentChar == ')') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.RPAREN, "" + temp);
            }

            if (currentChar == '^') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.POW, "" + temp);
            }
            if (currentChar == ':'){
                forward();
                if (currentChar == '='){
                    Character temp = currentChar;
                    forward();
                    return new Token(TokenType.ASSIGN, ":=");
                }
            }
            if (currentChar == '.'){
                forward();
                return new Token(TokenType.DOT, ".");
            }
            if (currentChar == ';'){
                forward();
                return new Token(TokenType.SEMI, ";");
            }
            throw new Exception("Parsing error!");
        }
        return new Token(TokenType.EOL, null);
    }

    private void skip() {
        while ((currentChar != null) && Character.isSpaceChar(currentChar)) {
            forward();
        }
    }

    private String number() throws Exception{
        String result = "";
        int dot = 0;
        while ((currentChar != null) && (Character.isDigit(currentChar) || currentChar.equals('.'))) {
            if (currentChar.equals('.')) {
                dot++;
                if (dot > 1) {
                    throw new Exception("Too many dot!!!");
                }
            }
            result += currentChar;
            forward();
        }
        return result;
    }

    private String var() throws Exception{
        StringBuilder result = new StringBuilder();
        while (Character.isAlphabetic(currentChar)){
            result.append(currentChar);
            forward();
        }
        return result.toString();
    }

    private void forward() {
        pos += 1;
        if (pos > text.length() - 1) {
            currentChar = null;
        }
        else {
            currentChar = text.charAt(pos);
        }
    }

    public Lexer(String text) {
        this.text = text;
        forward();
    }
}