package main;

public class Ring {
    private Element current;
    
    public Ring() {
        this.current = null;
    }
    
    public Ring(int n) {
        this();
        if (n == 1) {
            this.current = new Element(1, null);
            this.current.setNext(this.current);
        } else if (n > 1) {
            Element prev = new Element(2, null);
            this.current = new Element(1, prev); 
            for (int i = 3; i < n+1; i++) {
                prev.setNext(new Element(i, null));
                prev = prev.getNext();
            }
            prev.setNext(this.current);            
        }
    }
    
    public void next() {
        if (this.current == null)
            return;
        this.current = this.current.getNext();
    }
    
    public void back() {
        if (this.current == null)
            return;
        Element temp = this.current;
        while (this.current.getNext() != temp)
            this.next();
    }
    
    public int getCurrentElement() {
        return this.current.getData();
    }
    
    public void insert(int d) {
        if (this.current == null) {
            this.current = new Element(d, null);
            this.current.setNext(this.current); 
        } else {
            this.current.setNext(new Element(d, this.current.getNext()));
            this.next();            
        }            
    }
    
    public String toString() {
        if (this.current == null)
            return "null ";
        
        String temp = ""; 
        Element cur = this.current;
        do {
            temp += this.current.getData() + " ";
            this.next();
        } while (this.current != cur);
        return temp.substring(0, temp.length()-1);
    }
    
    public int remove() {
        if (this.current == null)
            return 0;
        
        int temp = this.current.getData();
        if (this.current.getNext() == this.current)
            this.current = null;
        else {
            this.back();
            this.current.setNext(this.current.getNext().getNext());
            this.next();
        }
        return temp;
    }
    
    public int everyNth(int n) {
        if (this.current == null)
            return 0;
        do {
            for (int i = 1; i < n; i++)
                this.next();
            this.remove();
        } while(this.current != this.current.getNext());
        return this.getCurrentElement();
    }
}

	


