package race;

public class Car {
	
	private String name;
	private Tires tires;
	private Motor motor;
	private Battery battery;
	
	public Car(String name, Tires tires, Motor motor, Battery battery) {
		
		this.name = name;
		this.tires = tires;
		this.motor = motor;
		this.battery = battery;
	}
	
	public Car(String name) {
		
		this.name = name;
	}
	
	public String getName() {
		
		return name;
	}
	
	public Tires getTires() {
		
		return tires;
	}
	
	public Motor getMotor() {
		
		return motor;
	}
	
	public Battery getBattery() {
		
		return battery;
	}
	
	public boolean isFunctional() {
		
		if (tires == null || motor == null || battery == null || battery.getCapacity() <= 0) {
			
			return false;
		} else
			return true;
		
	}
	
	public double calculateWeight() {
		
		return tires.getWeight() + motor.getWeight() + battery.getWeight();
	}
	
	public double calculateSpeed() {
		
		return ( tires.getCircumference() * motor.getPower() ) / (1000 * calculateWeight() );
	}
	
	public double calculateConsumption() {
		
		return ( motor.getPower() * calculateWeight() ) / 100;
	}
	
	public double raceStep() {
		
		double consumption = calculateConsumption(); 
		
		if (battery.reduceCapacity(consumption)) {
			
			return calculateSpeed();
			
		} else 
			return 0;
	}
	
	public void buildCar(Track track, Tires[] tires, Motor[] motors, Battery[] batteries) {
		
		Car[] goalCars = new Car[tires.length * motors.length * batteries.length]; 
		int carCounter = 0;
		
		for(int i = 0; i < tires.length; i++) {
			for(int j = 0; j < motors.length; j++) {
				for (int k = 0; k < batteries.length; k++) {
					
					Car car = new Car("fish", tires[i], motors[j], batteries[k]);
					
					double speed = car.calculateSpeed();
					double consumption = car.calculateConsumption();	
					
					int maxSteps = (int)(car.getBattery().getCapacity() / consumption);
					
					double maxDistance = maxSteps * speed;
					
					if (maxDistance >= track.getLength()) {
						
						goalCars[carCounter++] = car;
						
					}										
					
				}
			}
		}
		
		if (carCounter > 0) {
			Car fastestCar =  goalCars[0];
			
			for(int i = 0; i < carCounter; i++) {
				
				if (goalCars[i].calculateSpeed() > fastestCar.calculateSpeed() ) {
					
					fastestCar = goalCars[i];
				} 
			}
			
			this.tires = fastestCar.getTires();
			this.motor = fastestCar.getMotor();
			this.battery = fastestCar.getBattery(); 	
		}	
		
		
	}
	

}
