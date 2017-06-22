package main;

public class Element {
	
	private int data;
	private Element next;
	
	public Element(int data, Element next) {
		this.data = data;
		this.next = next;
	}
	
	public Element(int data) {
		this.data = data;
	}
	
	public Element(Element next) {
		this.next = next;
	}
	
	public Element() {

	}
	
	public int getData() {
		return data;
	}
	
	public void setData(int data) {
		this.data = data;
	}
	
	public Element getNext() {
		return next;
	}
	
	public void setNext(Element next) {
		this.next = next;
	}

}

