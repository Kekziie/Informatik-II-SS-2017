package race;

import java.lang.Math;

public class Tires {
	
	private double weight, diameter;
	
	public Tires(double diameter, double weight) {
		
		this.weight = weight;
		this.diameter = diameter;
	}
	
	public double getWeight() {
		
		return weight;
	}
	
    public double getDiameter() {
		
		return diameter;
	}
    
    public double getCircumference() {
		
		return diameter * Math.PI ;
	}

}
