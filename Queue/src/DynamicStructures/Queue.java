package DynamicStructures;

public class Queue {
	private ListElement first = null; 
	private ListElement last = null;
	
	public Queue() {
	}
	
	/**
	 * Adds a new ListElement to the end of this queue.
	 * 
	 * @param value the next list element's value.
	 */
	public void addElement(double value) {
		ListElement newLast = new ListElement(value);
		if (last != null) {
			last.setNextElement(newLast);
		} else {
			first = newLast;
		}
		last = newLast;
	}
		
	
	/**
	 * Removes and returns the first element in this queue.
	 * 
	 * @return the first element of this queue
	 */
	public ListElement popFirstValue() {
		if(first==null || last == null) {
			 return null;
		}
		ListElement firstElement = first;
		first = first.getNextElement();
		return firstElement;
	}
	
	/**
	 * Calculates the average of the values in this queue.
	 * 
	 * @return the average; NaN when the queue is empty.
	 */
	public double computeAverage() {
		if(first==null || last == null) {
			 return Double.NaN;
		} 
		double count=0;
		double sum = 0;
		ListElement element = first;
		while (element != null) {
			count++;
			sum += element.getValue();
			element = element.getNextElement();
		}
		return sum/count;	
	}
}
