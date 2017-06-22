package race;

public class Battery {
	
	private double capacity, weight;
	
	public Battery(double capacity, double weight) {
		
		this.capacity = capacity;
		this.weight = weight;
	}
	
	public double getCapacity() {
		
		return capacity;
	}
	
	public double getWeight() {
		
		return weight;
	}
	
	public boolean reduceCapacity(double value) {
		
		capacity -= value;
		if (capacity < 0) {
			
			capacity = 0;
			return false;
			
		} else 
			return true;
		 
	}

}
