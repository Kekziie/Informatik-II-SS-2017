package info2.ueb9.rpn.token;

/**
 * A sub interface for operator tokens.
 * @author Sebastian Otte
 */
public interface Operator extends Token {
    //
    public int getPrecedence();
}