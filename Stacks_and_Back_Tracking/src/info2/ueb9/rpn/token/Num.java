package info2.ueb9.rpn.token;

/**
 * This class represents a number (double) token.
 * @author Sebastian Otte
 */
public class Num implements Token {
    public final double value;
    
    public Num(final double value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    @Override
    public void accept(final TokenVisitor visitor) {
        visitor.handle(this);
    }

}

