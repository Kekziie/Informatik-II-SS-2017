package info2.ueb9.rpn;

import info2.ueb9.rpn.token.Div;
import info2.ueb9.rpn.token.LPar;
import info2.ueb9.rpn.token.Minus;
import info2.ueb9.rpn.token.Mul;
import info2.ueb9.rpn.token.Num;
import info2.ueb9.rpn.token.Operator;
import info2.ueb9.rpn.token.Plus;
import info2.ueb9.rpn.token.RPar;
import info2.ueb9.rpn.token.Token;
import info2.ueb9.rpn.token.TokenVisitor;

/**
 * ShuntingYard implemenation for transforming an infix expression
 * into a postfix expression.
 * @author Sebastian Otte
 */
public class ShuntingYard implements TokenVisitor {
        
    private TokenList  X;
    private TokenStack Y;
    private TokenList  Z;
    
    public ShuntingYard(final TokenList list) {
        this.X = list.copy();
        this.Y = new TokenStack();
        this.Z = new TokenList();
    }
    
    /**
     * Transforms the input token list (X) into a
     * postfix token list (Z).
     */
    public TokenList transform() {
    	for(int i = 0; i < X.sizeList(); i++) {
    		X.get(i).accept(this);
    	}
    	while (!Y.isEmpty()) {
    		Z.insertElemLast(Y.pop());
    	}
    	return Z;
    }
    
    @Override
    public void handle(final LPar token) {
    	Y.push(token);
    }

    
    /**
     * A common subroutine, which is used by all operators.
     */
    private void handleOperator(final Operator op) {
        while (!Y.isEmpty() && Y.top() instanceof Operator && 
        		op.getPrecedence() <= ((Operator)(Y.top())).getPrecedence()) {
        	Z.insertElemLast(Y.pop());
        }
        Y.push(op);
    }

    
        @Override
    public void handle(final Num token) {
       Z.insertElemLast(token);
    }

    
    @Override
    public void handle(final RPar token) {
        while (!Y.top().toString().equals("(")) {
        	Z.insertElemLast(Y.pop());
        }
        Y.pop();
    }
    
    @Override
    public void handle(final Plus token) {
        this.handleOperator(token);        
    }

    @Override
    public void handle(final Minus token) {
        this.handleOperator(token);        
    }


    @Override
    public void handle(final Mul token) {
        this.handleOperator(token);        
    }

    @Override
    public void handle(final Div token) {
        this.handleOperator(token);        
    }

    
}