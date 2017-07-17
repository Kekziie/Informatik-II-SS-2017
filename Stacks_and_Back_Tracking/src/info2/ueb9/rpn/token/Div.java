package info2.ueb9.rpn.token;

/**
 * This class represents a division token.
 * @author Sebastian Otte
 */
public class Div implements Operator {
    @Override
    public String toString() {
        return "/";
    }

    @Override
    public void accept(final TokenVisitor visitor) {
        visitor.handle(this);
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

}

    