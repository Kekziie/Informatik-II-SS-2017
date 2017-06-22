package race;

public class Motor {
	
	private double weight, power;
	
	public Motor(double power, double weight) {
		
		this.power = power;
		this.weight = weight;
	}
	
	public double getPower() {
		
		return power;
	}
	
	public double getWeight() {
		
		return weight;
	}

}
