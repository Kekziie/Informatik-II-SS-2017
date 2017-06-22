package race;

public class Main {
	
	public static void main(String[] args) {
		
		Tires tire1 = new Tires(10, 10);
		Tires tire2 = new Tires(20, 5);
		
		Motor motor1 = new Motor(10, 10);
		Motor motor2 = new Motor(1, 2);
		
		Battery battery1 = new Battery(10, 10);
		Battery battery2 = new Battery(50, 15);
		
		Track track1 = new Track(100);
		
	    Car car1 = new Car("fishi", tire2, motor2, battery2);
	    Car car2 = new Car("cat", tire1, motor1, battery1);

	    Car[] participatingCars = new Car[2];
	    participatingCars[0] = car1;
	    participatingCars[1] = car2;
	    
	    Race race = new Race(participatingCars, track1);
	    
	    Result[] results = race.calculateResults();
	    
	    System.out.println("The winner is " + results[0].getCar().getName());
	    System.out.println("Mi mi mi " + results[1].getCar().getName());
	}

}
