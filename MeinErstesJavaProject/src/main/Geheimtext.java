package main;

public class Geheimtext {
	
	public static void main(String[] args) {
		char [] zeichen = new char [95];
		// einlesen des Eingabetextes
		String eingabe = "aaa bbb CCC DDD"; 
		// umwandeln String zu Char-Array
		char[] inputArray = eingabe.toCharArray (); 
		char[] codeBook = getRandomCodeBook();
		char[] encodedArray = encode(inputArray, codeBook);
		char[] decodeBook = getDecodeBook(codeBook);
		char[] decodedArray = encode(encodedArray, decodeBook);
		System.out.println("druckbare Zeichen: ");
		System.out.println(getCharacterArray());
		System.out.println("Wort: ");
		System.out.println(toStringCharArray(inputArray));
		System.out.println("Codebook: ");
		System.out.println(toStringCharArray(codeBook));
		System.out.println("Kodiertes Wort: " );
		System.out.println(toStringCharArray(encodedArray));
		System.out.println("Dekodierbuch: ");
		System.out.println(toStringCharArray(decodeBook));
		System.out.println("entschluesseltes Wort: ");
		System.out.println(toStringCharArray(decodedArray));
		System.out.println("done");

	}
	//Liefert ein Array mit allen druckbaren Zeichen
	public static char[] getCharacterArray()					
	
	{															
		char[] arr = new char[95];
		for (int i= 0; i < arr.length; i++)
		{
			int k = i + 32;
			arr[i] = (char) (k);
			
		}
		return arr;
	}
	//a) Liefert String mit allen ASCII-Zeichen des Array
	 public static String toStringCharArray(char[] zeichen)		
	 {															
		 String ArrayString = "";
		 for (int i = 0; i < zeichen.length; i++)
		 {
			 ArrayString = ArrayString + zeichen[i];  
		 }
		 return ArrayString;
	 }
	 
	//b) Bekommt ein Array und mischt dessen Elemente Random durch
	 public static void shuffle (char[] zeichen)				 
	 {															
		 int N = zeichen.length;
		 for (int i = 0; i < (N - 1); i++)
		 {
			 int j = (int)(Math.random()*zeichen.length);
			 char temp = zeichen[i];
			 zeichen[i] = zeichen [j];
			 zeichen[j] = temp;
		 }
		
	 }
	 
	//c) Erstellt ein Char Array und mischt dieses durch = Codebook
	 public static char[] getRandomCodeBook()					
	 {															
		 char[] array2 = new char[95];
		 array2 = getCharacterArray();
		 shuffle(array2);
		 return array2;
	 }
	
	public static char[]encode(char [] array3, char []CodeBook){
		char [] arr4= new char [array3.length];
		for(int i=0;i<array3.length;i++)
		{
			int f= (int)array3[i]-32;
			arr4[i]=CodeBook[f];
		}	
				
			return arr4;
	}
	
	
	public static int Position(char zeichen, char[] array){
	for (int i= 0; i < array.length; i++)
	{
		if (zeichen == array[i])
			return i;
	}
	return 0;
	}

	// e) Dekodierungsbuch
	public static char [] getDecodeBook(char[]codeBook){
		char[] zeichen = getCharacterArray();
		char[] decode = new char [codeBook.length];
		for (int i= 0; i < codeBook.length; i++)
		{
			decode[i] = zeichen[Position(zeichen[i], codeBook)];
		}

		return decode;
		}
	}
	
