package main;


public class Optimize {

	public static final double A = 5;
	public static final double B = -2;

	public static void main(String[] args) {
	}

	public static double optimize(int func, double x0) {
		double result = 0;

		double x1=0;
		while (Math.abs(x0-(x0 - 0.001 * numericalGradient(func,x0)))>Math.pow(10, -9))
		{

			x1=x0;
			x0=(x1-0.001*numericalGradient(func,x1));
		}
		result = x1;

		return result;// TODO - return the actual result here
	}

	public static double evalFunc(int func, double x) {
		if(func==0)
			return f1(x);
		return f2(x);                                
	}

	public static double f1(double x) {
		return 1.84+1.42*x-2.4*Math.pow(x, 2)+0.91*Math.pow(x, 3)-0.124*Math.pow(x, 4)+ 0.0055*Math.pow(x, 5);

	}

	public static double f2(double x) {
		x=x-A;
		double result = Math.pow(x, 2)-Math.abs(B);
		return result;
	}        

	public static double numericalGradient(int func, double x) {
		; // TODO calculate the numerical gradient

		double result = 0;
		double h = 0.00001;
		if(func==0) result= (f1(x+h)-f1(x-h))/(2*h);
		else result= (f2(x+h)- f2(x-h))/(2*h);


		return result;

	}
}
