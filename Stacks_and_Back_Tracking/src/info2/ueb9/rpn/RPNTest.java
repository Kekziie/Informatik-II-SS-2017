package info2.ueb9.rpn;


/**
 * 
 * @author Sebastian Otte
 */
public class RPNTest {
        
    public static void test(
        final double result,
        final String expr
    ) {
        final TokenList infix   = TokenParser.parseInfixExpression(expr);
        final TokenList postfix = new ShuntingYard(infix).transform();
        
        System.out.println("infix: "   + infix);
        System.out.println("postfix: " + postfix);
        
        System.out.println("desired result: "  + result);
        System.out.println("computed result: " + RPNCalculator.compute(postfix));
    }
    
    
    public static void main(String[] args) {
        test(
            1.0 + 2.0 + 3.0 + 4.0 + 5.0 + 6.0 + 7.0 + 8.0,
            "1.0 + 2.0 + 3.0 + 4.0 + 5.0 + 6.0 + 7.0 + 8.0"
        );
        System.out.println();
        test(
            1.0 * 2.0 + 3.0 * 4.0 + 5.0 * 6.0 + 7.0 * 8.0,
            "1.0 * 2.0 + 3.0 * 4.0 + 5.0 * 6.0 + 7.0 * 8.0"
        );
        System.out.println();
        test(
            20.0 + 0.0 * 3.0 * 3.0 * 4.0 + 9.0*11.0/4.0*(2.0+(2.0+(9.0/3.0*3.0/2.0))),
            "20.0 + 0.0 * 3.0 * 3.0 * 4.0 + 9.0*11.0/4.0*(2.0+(2.0+(9.0/3.0*3.0/2.0)))"   
        );
        System.out.println();
        test(
            4.0 + 9.0 * 11.0/4.0,
            "4.0 + 9.0 * 11.0/4.0"
        );
        System.out.println();
        test(
            9.0 + 3.0 - 2.0 + 1.3,
            "9.0 + 3.0 - 2.0 + 1.3"
        );
        System.out.println();   
   }

    
    
}