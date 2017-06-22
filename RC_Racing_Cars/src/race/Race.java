package race;

public class Race {

	private Car[] participatingCars;
	private Track track;
	
	public Race(Car[] participatingCars, Track track) {
		
		this.participatingCars = participatingCars;
		this.track = track;
	}
	
	public Car[] getParticipatingCars() {
		
		return participatingCars;
	}
	
	public Track getTrack() {
		
		return track;
	}
	
	private Result[] sort(Result[] results) {
		for(int i = 0; i < results.length - 1; i++) {
			for(int j = 0; j < participatingCars.length -1 -i; j++) {
				if (results[j].getTime() > results[j+1].getTime()) {
					
					Result c = results[j];
					results[j] = results[j+1];
					results[j+1] =c;
				}
			}
		}
		return results;
	}
	
	public Result[] calculateResults() {
		
		Result[] results = new Result[participatingCars.length]; 
		double position = 0;
		double trackLength = track.getLength();
		
		for(int i = 0; i < participatingCars.length; i++) {
			
			if (!participatingCars[i].isFunctional()) {
				trackLength = Double.MAX_VALUE;
			}
			
			while (trackLength > position && participatingCars[i].isFunctional()) {
				
				position += participatingCars[i].raceStep();
			}
			
			results[i] = new Result(participatingCars[i], participatingCars[i].isFunctional(), trackLength / participatingCars[i].raceStep(), 0);
			}
		
		sort(results);
		for (int i = 0; i < results.length; i++) {
			
			if (results[i].getTrackFinished()) {
				
				results[i].setPlace(i + 1);
			} else 
				results[i] = new Result(participatingCars[i], participatingCars[i].isFunctional(), 0 , results.length);
		}
		return results;
	}	
}
