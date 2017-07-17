package info2.ueb9.rpn.token;

/**
 * This class represents a right parenthesis token.
 * @author Sebastian Otte
 */
public class RPar implements Token {
    @Override
    public String toString() {
        return ")";
    }
    
    @Override
    public void accept(final TokenVisitor visitor) {
        visitor.handle(this);
    }

}
