package DynamicStructures;

public class SimulateQueue {
	
	/**
	 * Fill queue with values starting from 0 until the provided size (-1).
	 * 
	 * @param size The number of elements that will be added to the new queue.
	 * @return the constructed queue
	 */
	public static Queue fillQueue(int size) {
		Queue newQueue = new Queue();
		for(int i = 0; i < size; i++) {
			newQueue.addElement(i);
		}
		return newQueue;
	}
	
	/**
	 * Empties the provided queue and generates a String of the values
	 * in the queue.
	 * 
	 * @param queue the queue that is to be emptied
	 * @return the constructed String from the values.
	 */
	public static String emptyQueue(Queue queue) {
		ListElement element;
		StringBuilder sb = new StringBuilder();
		while ((element = queue.popFirstValue()) != null) {
			sb.append(element.getValue());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	// testet die Methoden
	public static void main(String[] args) {
		System.out.println(emptyQueue(fillQueue(10)));
	}
}
