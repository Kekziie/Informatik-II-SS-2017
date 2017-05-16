package main;

import java.util.Arrays;

public class NaiveSorting {

	public static void main(String[] args) {
		// TODO: you can test your solution like this:
		
//		int[] myArray = new int[10];
//		for(int i=0; i<myArray.length; i++)
//			myArray[i] = (int)(Math.random()*100);
//		
//		System.out.println("Testing array:" + Arrays.toString(myArray));
//				
//		System.out.println("Minimum: "+getMinimum(myArray));
//		System.out.println("Maximum: "+getMaximum(myArray));
//		System.out.println("Mean: "+getMean(myArray));
//		
//		System.out.println(getArrayString(myArray,3));
//		
//		sortArray(myArray);
//		
//		System.out.println(getArrayString(myArray, 5));
	}

	/**
	 * Gets the minimum value in the array and return its value.
	 * 
	 * @param array
	 * @return the minimum
	 */
	public static int getMinimum(int[] array) {	
		// TODO a)
		int min = array[0];
		if (array.length == 0)
			return -1;
		else for (int i = 0, i <= array.length -1 , i++) {
			if (array[i] < min)
			{
				min=array[i];
			}
		}
		return min; // return the determined value!		
	}

	/**
	 * Get the maximum value in the array and return its value.
	 * 
	 * @param array
	 * @return the maximum
	 */
	public static int getMaximum(int[] array) {	
		// TODO a)
		int max = array[0];
		if (array.length == 0)
			return -1;
		else for (int i = 0, i <= array.length -1 , i++) {
			if (array[i] < max)
			{
				max=array[i];
			}
		}
		return max; // return the determined value!		
	}

	/**
	 * Gets the mean value in the array and return its value.
	 * 
	 * @param array
	 * @return the maximum
	 */
	public static double getMean(int[] array) {	
		// TODO b)
		return 0; // return the determined value!		
	}

	/**
	 * Sorts the array in increasing order.
	 */
	public static void sortArray(int[] array) {

		// TODO c)
	}

	/**
	 * Converts the array into a formatted string.
	 * 
	 * @param array the array
	 * @param numPerLine the array entries in the string per line
	 * @return the string representation.
	 */
	public static String getArrayString(int[] array, int numPerLine) {

		// TODO d
		return ""; // return the generated String here.
	}


}

