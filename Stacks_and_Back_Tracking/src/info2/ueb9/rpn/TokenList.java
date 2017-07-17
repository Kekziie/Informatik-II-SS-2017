package info2.ueb9.rpn;

import info2.ueb9.rpn.token.Token;

/**
 * Implementation of a Token list.
 * 
 * @author Martin Butz, Sebastian Otte
 */
public class TokenList {

	private TokenListElement head;
	private TokenListElement foot;

	public TokenList() {
		this.head = null;
		this.foot = null;
	}
	
	public TokenList copy() {
	    final TokenList result = new TokenList();
	    //
	    TokenListElement curr = this.head;
	    while (curr != null) {
	        result.insertElemLast(curr.item);
	        curr = curr.next;
	    }
	    //
	    return result;
	}

	public void insertElemFirst(final Token item) {
		if (this.head == null) {
			this.head = new TokenListElement(item, null);
			this.foot = this.head;
		} else {
			this.head = new TokenListElement(item, this.head);
		}
	}

	public void insertElemLast(final Token item) {
		if (this.head == null) {
			this.head = new TokenListElement(item, null);
			this.foot = this.head;
		} else {
			this.foot.next = new TokenListElement(item, null);
			this.foot      = this.foot.next;
		}
	} 

	public void clear() {
	    this.head = null;
	    this.foot = null;
	}
	
	public int sizeList() {
		if (this.head == null) {
			return 0;
		}
		//
		int i = 1;
		TokenListElement c = this.head;
		//
		while (c != this.foot) {
			i++;
			c = c.next;
		}
		return i;
	} 
	
	public Token get(final int index) {
	    TokenListElement curr = this.head;
	    //
	    int i = index;
	    //
	    while ((curr != null) && (i > 0)) {
	        i--;
	        curr = curr.next;
	    }
	    //
	    if ((curr != null) && (i == 0)) {
	        return curr.item;
	    }
	    return null;
	}
	
	public boolean isEmpty() {
		return this.head == null;
	}

	public void removeFirstElem() {
		if (this.head == null) {
		    return;
		}
		//
		if (this.foot == this.head) {
		    this.foot = null;
		}
		this.head = this.head.next;
	}

   public Token getFirstElem() {
        if (this.head == null) {
            return null;
        }
        return this.head.item;
    }

	public Token getLastElem() {
		if (this.head == null) {
		    return null;
		}
		return this.foot.item;
	}

	public void removeLastElem() {
		if (this.head == null) {
		    return;
		}
		//
		TokenListElement curr = this.head;
		//
		if (this.head == this.foot) {
		    this.head = null;
		    this.foot = null;
		    return;
		}
		//
		while (curr.next != this.foot) {
		    curr = curr.next;
		}
		//
		curr.next = null;
		this.foot = curr;
	}


	@Override
	public String toString() {
	    //
	    final StringBuilder out = new StringBuilder();
	    //
	    TokenListElement curr = this.head;
	    //
	    boolean first = true;
	    //
	    while (curr != null) {
	        if (!first) {
	            out.append(" ");
	        }
            out.append(curr.item);
	        curr = curr.next;
	        first = false;
	    }
	    //
	    return out.toString();
	}
	
	
}