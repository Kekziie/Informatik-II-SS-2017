package race;

public class Tires {
	
	private double weight = 10;
	private double diameter = 10;
	private double pi = 3.1415;
	
	public double getWeight() {
		
		return weight;
	}
	
	public double getDiameter() {
		
		return diameter;
	}
	
	public double getCircumference() {
		
		return diameter * pi;
	}

 }
