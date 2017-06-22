package main;

public class Main {
	
	public static void main(String[] args) {
		
		Ring r1 = new Ring();
		r1.back();
		System.out.println("Removing: " + r1.remove());
		
		Ring r2 = new Ring(10);
		System.out.println("" + r1 +" "+r2);
		r2.back();
		System.out.println(r2);
		r2.insert(42);
		System.out.println("Current Element = " + r2.getCurrentElement());
		r2.next();
		System.out.println(r2);
		r2.remove();
		r2.next();
		System.out.println(r2);
		System.out.println("Winner = " + r2.everyNth(7));
	}

}
