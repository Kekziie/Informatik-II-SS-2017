package info2.ueb9.rpn;

import info2.ueb9.rpn.token.Token;

/**
 * Implementation of a simple Token stack based on TokenList
 * @author Martin Butz, Sebastian Otte
 */
public class TokenStack {

    private TokenList list;
    
	public TokenStack() {
	    this.list = new TokenList();
	}
	
	public void push(final Token item) {
	    this.list.insertElemFirst(item);
	}
	
	
	
	public Token pop() {
	    final Token result = this.list.getFirstElem();
	    this.list.removeFirstElem();
	    return result;
	}
	
	public boolean isEmpty() {
	    return this.list.isEmpty();
	}
	

	public Token top() {
	    return this.list.getFirstElem();
	}

	
}