package main;

public class Optimize {

	public static final double A = 5;
	public static final double B = -2;
	
	public static void main(String[] args) {
	}

	public static double optimize(int func, double x0) {
		
		; // TODO - implement your optimization procedure here.
		
		return 0.00;// TODO - return the actual result here
	}
	
	public static double evalFunc(int func, double x) {
		if(func==0)
			return f1(x);
		return f2(x);				
	}
	
	public static double f1(double x) {
		
		return 0; // TODO - return the actual function value of f1 here.		
	}
	
	public static double f2(double x) {
		return 0; // TODO - return the actual function value of f2 here.
	}	

	public static double numericalGradient(int func, double x) {
		; // TODO calculate the numerical gradient
		
		return 0 ; // TODO return the determined gradient here.
	}

}
