package info2.ueb9.rpn.token;

/**
 * This class represents a minus token.
 * @author Sebastian Otte
 */
public class Minus implements Operator {
    
    @Override
    public String toString() {
        return "-";
    }
    @Override
    public void accept(final TokenVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

}

    