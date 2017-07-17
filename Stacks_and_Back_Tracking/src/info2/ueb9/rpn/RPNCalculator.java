package info2.ueb9.rpn;

import info2.ueb9.rpn.token.Div;
import info2.ueb9.rpn.token.LPar;
import info2.ueb9.rpn.token.Minus;
import info2.ueb9.rpn.token.Mul;
import info2.ueb9.rpn.token.Num;
import info2.ueb9.rpn.token.Plus;
import info2.ueb9.rpn.token.RPar;
import info2.ueb9.rpn.token.Token;
import info2.ueb9.rpn.token.TokenVisitor;

/**
 * An implementation of primitive stack calculator for computing
 * expression in reverse polish notation (postfix notation). This class
 * also implement the token visitor interface.
 * @author Sebastian Otte
 */
public class RPNCalculator implements TokenVisitor {
        
    private TokenStack Y;   // internal stack.
    
    public RPNCalculator() {
        this.Y = new TokenStack();
    }
    
    /**
     * Return the current result, that is, the top element on the stack.
     */
    public double getResult() {
        return num(this.Y.top());
    }
    
    /**
     * A subroutine for reading the value of a given token. If the given token
     * is an instannce of Num the methods returns its value. Otherwise a
     * runtime exception is thrown.
     */
    private static double num(final Token token) {
        if (token != null && token instanceof Num) {
            return ((Num)(token)).value;
        }
        throw new RuntimeException("RPNCalculator error - stack top is not a number.");
    }
    
    @Override
    public void handle(final RPar token) {
        //
        // ignore.
        //
    }
    
    @Override
    public void handle(final LPar token) {
        //
        // ignore.
        //
    }
    

    @Override
    public void handle(final Plus token) {
        Token a = Y.pop();
        a.accept(this);
        Token b = Y.pop();
        b.accept(this);
        Num c = (Num)Y.pop();
        Num d = (Num)Y.pop();
        Num result = new Num(d.value + c.value);
        Y.push(result);
    }

    @Override
    public void handle(final Minus token) {
    	Token a = Y.pop();
        a.accept(this);
        Token b = Y.pop();
        b.accept(this);
        Num c = (Num)Y.pop();
        Num d = (Num)Y.pop();
        Num result = new Num(d.value - c.value);
        Y.push(result);
    }


    @Override
    public void handle(final Mul token) {
    	Token a = Y.pop();
        a.accept(this);
        Token b = Y.pop();
        b.accept(this);
        Num c = (Num)Y.pop();
        Num d = (Num)Y.pop();
        Num result = new Num(d.value * c.value);
        Y.push(result);
    }

    @Override
    public void handle(final Div token) {
    	Token a = Y.pop();
        a.accept(this);
        Token b = Y.pop();
        b.accept(this);
        Num c = (Num)Y.pop();
        Num d = (Num)Y.pop();
        Num result = new Num(d.value / c.value);
        Y.push(result);
    }

    @Override
    public void handle(final Num token) {
        Y.push(token);
    }
    
    /**
     * This method encapsulates the computation process
     * for an entire list of token. The value of the top stack
     * token after processing the last input token is returned,
     * that is, the result of the given infix expression.
     */
    public static double compute(final TokenList list) {
        final RPNCalculator calc = new RPNCalculator();
        //
        final TokenList copy = list.copy();
        while (!copy.isEmpty()) {
            copy.getFirstElem().accept(calc);
            copy.removeFirstElem();
        }
        //
        return calc.getResult();
    }
    
    

    
    
}