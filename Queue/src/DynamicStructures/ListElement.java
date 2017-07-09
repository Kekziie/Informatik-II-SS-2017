package DynamicStructures;


public class ListElement {
    /**
     * Die Klasse implementiert die Elemente einer Liste.
     * Ein Listen-Element besteht  nur aus den Attributen, die 
     * - den Inhalt eines Knotens repräsentiert und 
     * - einen Zeiger auf ein Nachfolgeelement enthalten.
     */
	
	private final double VALUE;
	private ListElement nextElement;
	
	public ListElement(double val, ListElement next) {
		this.VALUE = val;
		this.nextElement = next;
	}
	
	public ListElement(double val) {
		this(val, null);
	}
	
	public double getValue() {
		return VALUE;
	}
	
	public ListElement getNextElement() {
		return nextElement;
	}
	
	public void setNextElement(ListElement newNext) {
		this.nextElement = newNext;
	}
}

