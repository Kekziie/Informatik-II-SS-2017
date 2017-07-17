package info2.ueb9.rpn;

import info2.ueb9.rpn.token.Div;
import info2.ueb9.rpn.token.LPar;
import info2.ueb9.rpn.token.Minus;
import info2.ueb9.rpn.token.Mul;
import info2.ueb9.rpn.token.Plus;
import info2.ueb9.rpn.token.RPar;
import info2.ueb9.rpn.token.Num;

/**
 * This is a simple implementation of token parser that
 * can parse infix expressions consisting of the terminal symbols
 *  '+', '-', '*', '/', '(', ')' as well as double literals.
 * @author Sebastian Otte
 */
public class TokenParser {

    private static char[] TOKEN_WHITESPACE = {' ', '\t'};
    private static char[] TOKEN_DIGIT  = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    
    private static char TOKEN_DOT   = '.';
    private static char TOKEN_PLUS  = '+';
    private static char TOKEN_MINUS = '-';
    private static char TOKEN_MUL   = '*';
    private static char TOKEN_DIV   = '/';
    
    private static char TOKEN_LPAR  = '(';
    private static char TOKEN_RPAR  = ')';
    
    private String str;
    private int    length;
    private int    ptr;
    
    public boolean isEOF() {
        return this.ptr >= this.length;
    }
    
    public TokenParser(final String str) {
        this.str    = str;
        this.ptr    = 0;
        this.length = str.length(); 
    }
    
    private char consumeChar() {
        return this.str.charAt(this.ptr++);
    }
    
    private char currentChar() {
        return this.str.charAt(this.ptr);
    }
    
    private static boolean isWhiteSpace(final char c) {
        return isOneOf(c, TOKEN_WHITESPACE);
    }
    
    private static boolean isOneOf(final char c, final char... chars) {
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                return true;
            }
        }
        return false;
    }
    
    private void nextToken() {
        while (!this.isEOF() && isWhiteSpace(this.currentChar())) {
            this.consumeChar();
        }
    }
    
    private int consumeDigits() {
        //
        int ctr = 0;
        //
        while (!this.isEOF() && isOneOf(this.currentChar(), TOKEN_DIGIT)) {
            ctr++;
            this.consumeChar();
        }
        //
        return ctr;
    }
    
    public boolean parsePlus(final TokenList list) {
        final int mark = this.ptr;
        this.nextToken();
        if (!this.isEOF() && (this.currentChar() == TOKEN_PLUS)) {
            this.consumeChar();
            list.insertElemLast(new Plus());
            return true;
        } else {
            this.ptr = mark;
            return false;
        }
    }
    
    public boolean parseMinus(final TokenList list) {
        final int mark = this.ptr;
        this.nextToken();
        if (!this.isEOF() && (this.currentChar() == TOKEN_MINUS)) {
            this.consumeChar();
            list.insertElemLast(new Minus());
            return true;
        } else {
            this.ptr = mark;
            return false;
        }
    }

    public boolean parseMul(final TokenList list) {
        final int mark = this.ptr;
        this.nextToken();
        if (!this.isEOF() && (this.currentChar() == TOKEN_MUL)) {
            this.consumeChar();
            list.insertElemLast(new Mul());
            return true;
        } else {
            this.ptr = mark;
            return false;
        }
    }
    
    public boolean parseDiv(final TokenList list) {
        final int mark = this.ptr;
        this.nextToken();
        if (!this.isEOF() && (this.currentChar() == TOKEN_DIV)) {
            this.consumeChar();
            list.insertElemLast(new Div());
            return true;
        } else {
            this.ptr = mark;
            return false;
        }
    }        

    public boolean parseLPar(final TokenList list) {
        final int mark = this.ptr;
        this.nextToken();
        if (!this.isEOF() && (this.currentChar() == TOKEN_LPAR)) {
            this.consumeChar();
            list.insertElemLast(new LPar());
            return true;
        } else {
            this.ptr = mark;
            return false;
        }
    }    
    
    public boolean parseRPar(final TokenList list) {
        final int mark = this.ptr;
        this.nextToken();
        if (!this.isEOF() && (this.currentChar() == TOKEN_RPAR)) {
            this.consumeChar();
            list.insertElemLast(new RPar());
            return true;
        } else {
            this.ptr = mark;
            return false;
        }
    }    
    
    public boolean parseNumber(final TokenList list) {
        final int mark = this.ptr;
        //
        this.nextToken();
        if (this.isEOF()) return false;
        //
        final int start = this.ptr;
        //
        // parse sign optional.
        //
        if (isOneOf(this.currentChar(), TOKEN_MINUS, TOKEN_PLUS)) {
            this.consumeChar();
        }
        //
        final int digits1 = this.consumeDigits();
        //
        // dot and following digits required if digits1 == 0;
        //
        if (!this.isEOF() && (this.currentChar() == TOKEN_DOT)) {
            this.consumeChar();
        } else if (digits1 == 0) {
            this.ptr = mark;
            return false;
        }
        //
        final int digits2 = this.consumeDigits();
        if ((digits2 == 0) && (digits1 == 0)) {
            this.ptr = mark;
            return false;
        }
        //
        // at this point there was a well-formed number (double).
        //
        final double value = Double.parseDouble(this.str.substring(start, this.ptr));
        list.insertElemLast(new Num(value));
        return true;
    }
    
    public boolean parseOperator(final TokenList list) {
        if (this.isEOF()) return false;
        if (this.parsePlus(list)) return true;
        if (this.parseMinus(list)) return true;
        if (this.parseMul(list)) return true;
        if (this.parseDiv(list)) return true;
        //
        return false;
    }
    
    public boolean parseAtomicExpr(final TokenList list) {
        //
        if (this.isEOF()) return false;
        final int mark = this.ptr;
        //
        if (this.parseNumber(list)) return true;
        //
        if (this.parseLPar(list)) {
            if (this.parseInfixExpression(list)) {
                if (this.parseRPar(list)) {
                    return true;
                }
            }
        }
        //
        this.ptr = mark;
        return false;
    }
    
    public boolean parseInfixExpression(final TokenList list) {
        //
        if (this.isEOF()) return false;
        //
        final int mark = this.ptr;
        //
        if (parseAtomicExpr(list)) {
            if (parseOperator(list)) {
                if (parseInfixExpression(list)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        //
        this.ptr = mark;
        return false;
    }

    /**
     * This method parses a given infix expression string and generates a list of
     * Token instances representing the particular atomic components of the expression. 
     * When the string could not be parsed, e.g. because it is malformed, null is returned.
     */
    public static TokenList parseInfixExpression(final String str) {
        
        final TokenParser parser = new TokenParser(str);
        final TokenList   list  = new TokenList();
        
        if (parser.parseInfixExpression(list)) {
            return list;
        }
        
        return null;
        
    }
}