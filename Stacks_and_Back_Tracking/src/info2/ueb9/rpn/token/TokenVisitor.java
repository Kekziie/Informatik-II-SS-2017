package info2.ueb9.rpn.token;


/**
 * Basis interface for a token visitor (cf. vistor pattern). It
 * contains one method for each particular token type.
 * @author Sebastian Otte
 */
public interface TokenVisitor {
    public void handle(final LPar token);
    public void handle(final RPar token);
    public void handle(final Plus token);
    public void handle(final Minus token);
    public void handle(final Mul token);
    public void handle(final Div token);
    public void handle(final Num token);
}