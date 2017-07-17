package info2.ueb9.rpn;

import info2.ueb9.rpn.token.Token;

/**
 * List element for a list of Token instances.
 * 
 * @author Martin Butz, Sebastian Otte
 */
public class TokenListElement {
    public Token item;
    public TokenListElement next;

    public TokenListElement(final Token item, final TokenListElement next) {
        this.item = item;
        this.next = next;
    }
}


