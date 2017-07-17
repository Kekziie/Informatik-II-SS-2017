package info2.ueb9.rpn.token;

/**
 * Basis token interface.
 * @author Sebastian Otte
 */
public interface Token {
    public void accept(final TokenVisitor visitor);
}