package main;

public class PeanoArithmetic {

    public static int incr(final int n) { return n + 1; }
    public static int decr(final int n) { return n - 1; }
    
    public static boolean isZero(final int n) { return n == 0; }
    public static boolean isSmallerZero(final int n) { return n < 0; }
    
    // soll a+b berechnen
    public static int add(int a,int b) {
    	return (isZero(b)) ? a : add(incr(a), decr(b)); 
    }
    
    // soll a-b berechnen
    public static int sub(int a, int b) {
    	return (isZero(b)) ? a : sub(decr(a), decr(b)); 
    }
    
    // soll a*b berechnen
    public static int mul(int a, int b) {
    	if (isZero(a) || (isZero(b))) 
    		return sub(a,a);
    	if (isZero(decr(a)))
    		return b;
    	return add((mul(decr(a),b)),b);
    }
   
    // soll a/b berechnen
    public static int div(int a, int b) {
    	if (isSmallerZero(sub(a,b)) || (isZero(b))) {
    		return 0;
    	} else {
    		return add(1, (div(sub(a,b),b)));
    	}
    }
   
    //  testen der Funktionen
    public static void main(String[] args) {
    	System.out.println(add(3,4));
    	System.out.println(sub(3,4));
    	System.out.println(mul(3,4));
    	System.out.println(div(10,4));
    }
}

