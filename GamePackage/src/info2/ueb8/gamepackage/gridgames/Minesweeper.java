package info2.ueb8.gamepackage.gridgames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import info2.ueb8.gamepackage.GameScores;
import info2.ueb8.gamepackage.GridGameInterface;
import info2.ueb8.gamepackage.GridGameTools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Minesweeper implements GridGameInterface, MouseListener {
	
	// Spielbereich
	private Rectangle gameArea;
	
	// Startbereich
    private Rectangle controlArea;
    
    
    private JPanel myPanel;
	
	// Matrix der Minen
	boolean[][] mines;
	
	// Matrix der Zahlen, 
	// die angibt wieviele Minen in der Umgebung sind
	int[][] fieldsNumber;
	
	// Matrix der "offenen" Felder
	boolean[][] openFields;
		
	// Matrix der Flaggen
	boolean[][] flags;	
	
	// Breite und Hoehe eines Feldes
	public static int TileSize = 35;
	
	// Anzahl der Minen
	int numberOfMines = 50;
	
	// Anzahl der Flaggen
	int flagCounter;
	
	// Anzahl der geschlossenen Felder
	int closeFields;
	
	boolean gameRunning;
	
	public Minesweeper(JPanel yourPanel, Rectangle gameArea, Rectangle controlArea) {
        this.gameArea = new Rectangle(gameArea);
        this.controlArea = new Rectangle(controlArea);

        myPanel = yourPanel;

        initGame();

        myPanel.addMouseListener(this); 
	}
 
	public void initGame() {
		gameRunning = true;
		
		// Anzahl der Felder in einer Reihe
		int numX = gameArea.width / TileSize;
		// Anzahl der Felder in einer Spalte
		int numY = gameArea.height / TileSize;
		
		fieldsNumber = new int[numX][numY];
		mines = new boolean[numX][numY];
		openFields = new boolean[numX][numY];
		flags = new boolean[numX][numY];
		
		flagCounter = 0;
		closeFields = numX * numY;
		
		myPanel.repaint();
	}
	
	// generiert Minen im Spielbereich auf zufaellige Felder
	private void generateMines(int x, int y) {
		Point[] fields = surroundedFields(x, y);
		// zaehlt uebrige Minen
		int counterMines = numberOfMines;
		while (counterMines > 0) {
			int randomX = ThreadLocalRandom.current().nextInt(mines.length);
			int randomY = ThreadLocalRandom.current().nextInt(mines[0].length);
			
			boolean isSurrounded = false;
			
			// ueberprueft, ob Feld schon "bemint" und nicht auf Feld (x,y)
			if (!mines[randomX][randomY] && randomX != x && randomY != y) {
				for(int i = 0; i < fields.length; i++) {
					if (randomX == fields[i].x && randomY == fields[i].y) {
						isSurrounded = true;
						break;
					}
				}
				if(isSurrounded)
					continue;
				mines[randomX][randomY] = true;
				counterMines--;
			}
		}
	}
	
	// setzt Zahlen +1 um Minenfelder
	private void generateNumbers() {
		for(int i = 0; i < mines.length; i++) {
			for(int j = 0; j < mines[0].length; j++) {
				if (mines[i][j]) {
					Point[] fields = surroundedFields(i, j);
					for(int k = 0; k < fields.length; k++) {
						fieldsNumber[fields[k].x][fields[k].y]++;
					}
				}
			}
		}
	}
	
	// ermittelt alle 8 umliegenden Felder, falls vorhanden
	private Point[] surroundedFields(int x, int y) {
		List<Point> result = new ArrayList<Point>();
		// oben
		if (y > 0)
			result.add(new Point(x, y-1));
		// oben rechts
		if (x < fieldsNumber.length -1 && y > 0)
			result.add(new Point(x+1, y-1));
		// rechts
		if (x < fieldsNumber.length -1)
			result.add(new Point(x+1, y));
		// rechts unten
		if (x < fieldsNumber.length -1 &&  y < fieldsNumber[0].length -1)
			result.add(new Point(x+1, y+1));
		// unten
		if (y < fieldsNumber[0].length -1)
			result.add(new Point(x, y+1));
		// unten links
		if (x > 0 && y < fieldsNumber[0].length -1)
			result.add(new Point(x-1, y+1));
		// links
		if (x > 0)
			result.add(new Point(x-1, y));
		// links oben
		if (x > 0 && y > 0)
			result.add(new Point(x-1, y-1));
		return result.toArray(new Point[0]);
	}
	
	// ermittelt, ob Spiel verloren ist
	private boolean lostGame(int x, int y) {
		if (mines[x][y]) {
			openFields[x][y] = true;
			myPanel.repaint();
			return true;
		}
		return false;
	}
	
	// zeigt Todesnachricht an, wenn Spiel verloren
	private void showDeathMessage() {
		JOptionPane.showMessageDialog(myPanel, "Btooomm! You died. Try again.");
		gameRunning = false;
	}

	// Met6hode gibt an, was passiert wenn man klickt
	public void mouseClicked(MouseEvent arg0) {
		if (GridGameTools.containedInWhichArea(arg0.getX(), arg0.getY(), gameArea) != 0) 
            return;
		
		if(!gameRunning) 
			return;	
		
		// ermitteln der Position, wo Maus geklickt hat
		int mouseX = arg0.getX() - gameArea.x;
		int mouseY = arg0.getY() - gameArea.y;
		
		// ermitteln des Feldes des Mausklicks
		int fieldX = mouseX / TileSize;
		int fieldY = mouseY / TileSize;
		
		// linke Maustaste
		if(arg0.getButton() == MouseEvent.BUTTON1) {
			// ueberprueft, ob Flagge auf dem Feld ist
			if (!flags[fieldX][fieldY]) {
				// generieren der Minen und Zahlen nach dem ersten Klick
				if (closeFields == openFields.length*openFields[0].length) {
					generateMines(fieldX,fieldY);
					generateNumbers();
				}
				
				// ueberprueft, ob Mine angeklickt wurde
				if (lostGame(fieldX, fieldY)) {
					showDeathMessage();
					return;
				}
				
				// ueberpreuft, ob Feld schon offen ist
				if (!openFields[fieldX][fieldY]) {
					// klicken auf ein Feld mit Zahl 0
					if (fieldsNumber[fieldX][fieldY] == 0) 
						openArea(fieldX, fieldY);
					else {				
						openFields[fieldX][fieldY] = true;
						closeFields--;
					}
				}
				myPanel.repaint();
			}
		}
		
		// rechte Maustaste
		if(arg0.getButton() == MouseEvent.BUTTON3) {
			// ueberpruft, ob Feld schon offen ist
			if (!openFields[fieldX][fieldY]) {
				//setzen und wegnehmen der Flagge
				flags[fieldX][fieldY] = !flags[fieldX][fieldY];
				myPanel.repaint();
				//wenn Flagge setzen, dann Zè‡§ler +1
				if (flags[fieldX][fieldY])  
					flagCounter++;
				//wenn Flage wegnehmen, dann Zè‡§ler -1
				else
					flagCounter--;
			}
		}
		
		// mittlere Maustaste
		if(arg0.getButton() == MouseEvent.BUTTON2) {
			if(openFields[fieldX][fieldY]) {
				Point[] fields = surroundedFields(fieldX, fieldY);
				boolean foundMine = false;
				for(int i = 0; i < fields.length; i++) {
					// ueberprueft, ob Feld von Flagge geschuetzt
					if(!flags[fields[i].x][fields[i].y]) {
						openArea(fields[i].x,fields[i].y);
						// wenn mind. ein Mine gefunden wird
						if (lostGame(fields[i].x, fields[i].y))
							foundMine = true;
					}
				}
				if(foundMine)
					showDeathMessage();
				myPanel.repaint();
			}
		}
		
		// ueberprueft, ob alle Felder, ausser Minenfelder, angeklickt wurden
		if(closeFields == numberOfMines && gameRunning)
			JOptionPane.showMessageDialog(myPanel, "Congratulation! You found all mines.");
	}
	
	// Methode, die den Bereich von Feldern mit der Zahl 0 freilegt
	private void openArea(int newX, int newY) {
		// Ausbreitung nur im Spielbereich moeglich
		if(newX < 0 || newX >= openFields.length || newY < 0 || newY >= openFields[0].length)
			return;
		// Flagge beschuetzen Feld vom freilegen
		if(openFields[newX][newY] || flags[newX][newY]) {
			return;
		}
		//Feld oeffnen
		openFields[newX][newY] = true;
		closeFields--;
		if(fieldsNumber[newX][newY] == 0) {
			Point[] fields = surroundedFields(newX, newY);
			for(int i = 0; i < fields.length; i++) {
				openArea(fields[i].x, fields[i].y);
			}
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	// bei Klick wird ein neues Spiel initialisiert
	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX();
        int mouseY = e.getY();
		if (GridGameTools.containedInWhichArea(mouseX, mouseY, controlArea) == 0) {
            initGame();
		}
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	// zeichnet rechts oben die Anzahl der verbleibenden Minen
	public GameScores getScores() {
		return GameScores.createGameScores(new String[]{"remaining Mines","closed fields"}, 
				new int[]{numberOfMines - flagCounter, closeFields});
	}

	public void closeGame() {
	}

	public void paintGameArea(Graphics2D g) {
		g.setFont(new Font("Consolas", 0, 27));
		g.setColor(Color.BLACK);
		
		for(int i = 0; i < openFields.length; i++) {
			for(int j = 0; j < openFields[0].length; j ++) {
				// berechnet x und y-Position des jeweiligen Feldes
				int posX = gameArea.x + i*TileSize;
				int posY = gameArea.y + j*TileSize;
				
				// ordnet Farben zu
				// geschlossen grau
				// offen weiss
				if(!openFields[i][j]) 
					g.setColor(Color.LIGHT_GRAY);
				else
					g.setColor(Color.WHITE);
				
				// zeichnet ausgemalte Felder mit obiger Farbe
				g.fillRect(posX, posY, TileSize, TileSize);
				
				// zeichnet schwarze Umrandung fãƒ» jedes Feld
				g.setColor(Color.BLACK);
				g.drawRect(posX, posY, TileSize, TileSize);
				
				if(flags[i][j]) {
					// zeichnet rote Dreiecke fuer Flaggen
					Polygon p = new Polygon();
					p.addPoint(posX + TileSize / 2, posY );
					p.addPoint(posX + TileSize, posY + TileSize);
					p.addPoint(posX, posY + TileSize);
					
					g.setColor(Color.RED);
					g.fillPolygon(p);
				} else if (openFields[i][j]) {
					
					if (mines[i][j]) {
						//zeichnet schwarze Minen aufs Feld
						g.setColor(Color.BLACK);
						g.fillOval(posX, posY, TileSize, TileSize);
					} else if(fieldsNumber[i][j] != 0) {

						// gibt Zahlen unterschiedliche Farben
						if(fieldsNumber[i][j] == 1)
							g.setColor(Color.GREEN);
						if(fieldsNumber[i][j] == 2)
							g.setColor(Color.BLUE);
						if(fieldsNumber[i][j] == 3)
							g.setColor(Color.ORANGE);
						if(fieldsNumber[i][j] == 4)
							g.setColor(Color.PINK);
						if(fieldsNumber[i][j] == 5)
							g.setColor(Color.MAGENTA);
						if(fieldsNumber[i][j] == 6)
							g.setColor(Color.CYAN);
						if(fieldsNumber[i][j] == 7)
							g.setColor(Color.YELLOW);
						if(fieldsNumber[i][j] == 8)
							g.setColor(Color.RED);
						
						//zeichnet Zahl ins Feld
						g.drawString(Integer.toString(fieldsNumber[i][j]), posX + (int)(TileSize * 0.25), posY + (int)(TileSize * 0.75));
					}		
				}
			}
		}
		
	}

	public void paintControlArea(Graphics2D g) {
		// zeichnet links oben ins Fenster ein New Game-Button
		 g.setColor(Color.black);
		 g.drawRect(controlArea.x, controlArea.y, controlArea.width, controlArea.height);
		 g.drawString("New Game", controlArea.x + 5, 
				 controlArea.y + controlArea.height / 2 + g.getFont().getSize() / 2);
	    }

	public String getName() {
		return "Minesweeper";
	}

}
