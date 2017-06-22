package race;

public class Result {
	
	private Car car;
	private boolean trackFinished;
	private double time;
	private int place; 
	
	public Result(Car car, boolean trackFinished, double time, int place) {
		
		this.car = car;
		this.trackFinished = trackFinished;
		this.time = time;
		this.place = place;
		
	}
	
	public Car getCar() {
		
		return car;
	}
	
	public boolean getTrackFinished() {
		
		return trackFinished;
	}
	
	public double getTime() {
		
		return time;
	}
	
	public int getPlace() {
		
		return place;
	}
	
	public void setPlace(int place) {
		
		this.place = place;
	}
	
	

}
