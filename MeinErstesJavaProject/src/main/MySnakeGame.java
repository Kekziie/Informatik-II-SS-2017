

// Die folgenden imports sind notwendig, um die Grafik zu unterstuetzen.
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * @author Martin V. Butz 11 / 2013
 */
@SuppressWarnings("serial")
public class MySnakeGame extends JPanel implements KeyListener{

	/**
	 * The colors that are supported by the game.
	 */
	public static Color snakeHead = new Color(250,10,0);	// rot
	public static Color snakeBody = new Color(128,0,0); 	// dunkelrot
	public static Color bgColor = new Color(0,150,230); 	// hellblau
	public static Color foodColor = new Color(0,128,0); 	// dunkelgruen
	public static Color goldFoodColor = new Color(255, 215, 0); // gold
	public static Color borderColor = new Color(0, 0, 0); 	// schwarz

	/**
	 * Groesse des Fensters, in dem das Spiel stattfindet.
	 */
	private static final int FRAME_WIDTH = 600, FRAME_HEIGHT = 500;

	/**
	 * Groesse der Game area, in dem das Spiel stattfindet (in pixel).
	 */
	private static int panelWidth, panelHeight;

	/**
	 * Lokation der Game Area im Panel (von links, in pixel). 
	 */
	private static int gameAreaLeft; 

	/**
	 * Lokation der Game Area im Panel (von oben, in pixel). 
	 */
	private static int gameAreaTop;

	/**
	 * Breite der Game Area (in pixel). 
	 */
	private static int gameAreaWidth;

	/**
	 * Hoehe der Game Area (in pixel). 
	 */
	private static int gameAreaHeight; 

	/**
	 * Groesse des Spielfeldes als Grid (Matrix)
	 * Anzahl Reihen.
	 */
	public static int numRows;

	/**
	 * Groesse des Spielfeldes als Grid (Matrix)
	 * Anzahl Spalten.
	 */
	public static int numColumns;

	/**
	 * The Location of the Snake (in der Matrix) 
	 */
	private static int[] snakeLoc;

	/**
	 * The direction towards which the snake is heading. 
	 * Note: the snake is always moving!
	 *  Konvention hier: 0 := North (nach oben);  1 := East (nach rechts); 
	 *                   2 := South (nach unten); 3 := West (nach links)
	 */
	private static int snakeDirection = 0; 

	/**
	 * In lastSnakeDirection sollte der letzte Richtungsschritt stehen, 
	 * den die Schlange auch aufgefuehrt hat.
	 * Dadurch wird verhindert, dass man nicht zweimal ohne sich zu bewegen die Richtung
	 * wechselt... und dadurch insbesondere auch in sich selbst laufen kann 
	 * (was verhindert werden soll).  
	 */
	private static int lastSnakeDirection = snakeDirection;

	/**
	 * The Location of the Food item (in der Matrix als 2-elementiger Array)  
	 */
	private static int[] foodLoc;

	/**
	 * Number of iterations played in the game.
	 */
	public static int iteration = 0;

	/**
	 * Number of food items that were eaten.
	 */
	private static int foodEaten = 0;

	/**
	 * Size of a tile - and thus also of one snake body part.
	 */
	private static final int TILE_SIZE = 15;

	/**
	 * Statisches Spiel-Objekt, das in der main Methode kreiert wird. 
	 */
	private static MySnakeGame mySnakeGame = null;

	/**
	 * The frequency of a move - every gameSpeed Milliseconds, the snake moves a step.
	 */
	private static long gameSpeed = 100;

	/**
	 * The main class to start this game. 
	 * 
	 * @param args Arguments are not considered here.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// Jetzt wird hier erst mal das Spielbereichsfenter generiert... 
		// Das bitte am besten einfach alles so stehen lassen... 
		JFrame frame = new JFrame("My Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		// Der Spielbereich ist etwas kleiner als das gesamte Fenster - wird nun bestimmt:
		panelWidth = frame.getContentPane().getWidth();
		panelHeight = frame.getContentPane().getHeight();

		// Nun muessen wir noch den Spielbereich kreieren und in das Fenster einfuegen. 
		// Auch dies sollte so stehen bleiben. 
		mySnakeGame = MySnakeGame.initFullSnakeGamePanel();
		frame.add(mySnakeGame);
		// Dem Fenster wird jetzt nocht mitgeteilt, dass auf Aktionen des Keyboards "hoeren" soll... 
		// Dadurch wird, wenn das Fenster aktiviert ist und man eine Taste drueckt
		// die Methode (siehe deren Implementierung weiter unten) KeyTyped(...) aufgerufen.
		frame.addKeyListener(mySnakeGame);
		// Um sicher zu gehen, dass die Spielumgebung auch angezeigt wird, noch dieser Befehl:
		frame.validate();
		// Nun geht es los mit dem Spiel.
		runGame();
		// Wenn das Spiel zu Ende ist, wird noch das Fenster geschlossen.
		frame.dispose();
	}


	/**
	 * Die Hauptschleife des Spiels ist hier implementiert. 
	 * 
	 * @throws InterruptedException
	 */
	private static void runGame() throws InterruptedException
	{
		boolean stillAlive = true; // flag um einen Zusammenstoss zu vermerken
		
		while(stillAlive) {
			// Schleife laeuft solange es keinen Zusammenstoss gab und 
			// noch nicht die maximale Laenge erreicht wurde.
			mySnakeGame.repaint();
			// Warten bis es Zeit ist fuer den naechsten Schritt ist. 
			Thread.sleep(gameSpeed);

			// Teste den Bewegungsschritt der Schlange.
			int x = snakeLoc[0];
			int y = snakeLoc[1];
			switch(snakeDirection){
			case 0: // North
				y--;
				if(y < 0)
					stillAlive = false;
				break;
			case 1: // EAST
				x++;
				if(x >= numColumns)
					stillAlive = false;
				break;
			case 2: // SOUTH
				y++;
				if(y >= numRows)
					stillAlive = false;
				break;
			case 3: // WEST
				x--;
				if(x < 0)
					stillAlive = false;
				break;
			}
			// Fuehre die Bewegung jetzt endgueltig aus.
			snakeLoc[0] = x;
			snakeLoc[1] = y;
			// merke die so eben gegangene richtung auf - damit durch Tastendruecke die naechste Richtung gewaehlt werden kann.
			lastSnakeDirection = snakeDirection;

			if(snakeLoc[0]==foodLoc[0] && snakeLoc[1]==foodLoc[1]) {
				// at food location --- am eating food. 
				foodEaten++;
				resetFoodLocation();
			}
			iteration++;
		}

		if(stillAlive) {
			// Mittels diesem Befehl, wird ein kleines Fenster geoeffnet in dem
			// der spezifizierte Text dargestellt wird. 
			// Durch Druecken auf den OK Button schliesst sich das Fenster dann wieder
			// und das Programm laeuft weiter. 
			JOptionPane.showMessageDialog(mySnakeGame, "Gewonnen !!!\n"
					+ "Du hast "+iteration+" Schritte gebraucht um \n"
					+ "alle moeglichen " + foodEaten + " Punkte zu fressen.");

		}else{
			JOptionPane.showMessageDialog(mySnakeGame, "Sorry - das war's!\n"
					+ "Du hast "+iteration+" Schritte durchgehalten\n"
					+ "und dabei " + foodEaten + " Punkte gefressen.");
		}
	}

	/**
	 * Setzt das Essen an eine neue Stelle.
	 */
	private static void resetFoodLocation()
	{
		foodLoc[0] = (int)(Math.random() * numColumns);
		foodLoc[1] = (int)(Math.random() * numRows);
		// TODO Stelle sicher, dass das Essen nicht in die Schlange selber plaziert wird.
	}

	/**
	 * Konstruktor fuer ein Graphik-Objekt. 
	 * Benoetigen wir, um einen Key-Listener auf dieses JPanel kreieren zu koennen.
	 * Ist leer, aber ganz wichtig, denn durch das Kreieren, wird indirekt die ganze 
	 * JPanel Funktionalitaet kreiert, weil ja diese Klasse JPanel "erweitert"...
	 * Wenn man das im Moment noch nicht so ganz versteht, dann ist das schon OK :-) 
	 */
	private MySnakeGame()
	{
		// in diese Methode bitte nichts hinein programmieren :-)
	}

	/**
	 * Initializes a graphics panel object of the desired width and height. 
	 * Bedeutet: Hier wird der Spielbereich kreiert und als Objekt zurueckgeliefert. 
	 * 
	 * @param width
	 * @param height
	 * @return The graphics JPanel object with the initialized game.
	 */
	public static MySnakeGame initFullSnakeGamePanel()
	{
		// Reihen = Anzahl Reihen minus zwei fuer den Rand
		// Vorsicht: Reihen bestimmen die Hoehe des Feldes NICHT die Breite!!!
		numRows = panelHeight/TILE_SIZE -2;
		// Spalten = Anzahl Spalten minus zwei fuer den Rand
		numColumns = panelWidth/TILE_SIZE -2;

		gameAreaWidth = numColumns * TILE_SIZE;
		gameAreaHeight = numRows * TILE_SIZE;
		gameAreaLeft = (panelWidth - gameAreaWidth) / 2;
		gameAreaTop = (panelHeight - gameAreaHeight) / 2;

		snakeLoc = new int[2];
		snakeLoc[0] = numColumns/2;
		snakeLoc[1] = numRows/2;

		foodLoc = new int[2];
		resetFoodLocation();

		iteration = 0;
		snakeDirection = 0; 

		// Hier wird jetzt noch der ganze Spielbereich - so zu sagen - ins Leben gerufen... 
		// Bitte das einfach so stehen lassen!
		MySnakeGame mySnakeG = new MySnakeGame();//width, height);
		// Und nun wird noch Spielbereichsobjekt zurueck gegeben - bitte unbedingt so stehen lassen.
		return mySnakeG;
	}

	/* ##################################################################################################
	 * ################## Methods to Paint the Board including all three areas ##########################
	 * ########## -> fuer die Lesbarkeit des Codes durch dieses Kommentar etwas abgetrennt <- ###########
	 * ##################################################################################################
	 */

	/**
	 * Draws the game board into this Panel within the frame.
	 * Diese Methode wird aufgerufen wenn diese Komponente - 
	 *     naemlich dieses JPanel bzw. dieser Spielebereich -
	 *     sich neu aufbauen bzw. malen sollen. 
	 * Die Methode kann indirekt ueber den repaint() Befehl bzw. durch mySnakeGame.repaint() aufgerufen werden. 
	 */
	public void paintComponent(Graphics gg) {
		// Zunaechste muss der gesamte Bereich gemalt werden (bitte so stehen lassen).
		super.paintComponent(gg); 
		// Dann werden die Raender gemalt.
		paintGameArea(gg);
		// Dann der Essenspunkt.
		paintFood(gg);
		// Dann die Schlange.
		paintSnake(gg);
	}

	/**
	 * This method paints the game area and the border. 
	 * 
	 * @param g
	 */
	public static void paintGameArea(Graphics g)
	{		
		g.setColor(borderColor);
		g.fillRect(0,  0,  panelWidth,  panelHeight);

		g.setColor(bgColor);
		g.fillRect(gameAreaLeft,  gameAreaTop,  gameAreaWidth, gameAreaHeight);
	}

	/**
	 * This methods paints the food item to the graphics object. 
	 * 
	 * @param g
	 */
	public static void paintFood(Graphics g)
	{
		g.setColor(foodColor);
		g.fillRect(gameAreaLeft + foodLoc[0]*TILE_SIZE, gameAreaTop + foodLoc[1]*TILE_SIZE, 
				TILE_SIZE, TILE_SIZE);
	}

	/**
	 * This methods paints the snake into the game area 
	 * at its current location. 
	 * 
	 * @param g
	 */
	public static void paintSnake(Graphics g)
	{
		g.setColor(snakeBody);
		// TODO Hier noch den Koerper der Schlange malen.
		g.setColor(snakeHead);
		g.fillRect(gameAreaLeft + snakeLoc[0]*TILE_SIZE, gameAreaTop + snakeLoc[1]*TILE_SIZE, 
				TILE_SIZE, TILE_SIZE);
	}
	

	/* ##################################################################################################
	 * ####################### Methods to handle the relevant key presses ###############################
	 * ########## -> fuer die Lesbarkeit des Codes durch dieses Kommentar etwas abgetrennt <- ###########
	 * ##################################################################################################
	 */

	/**
	 * Diese Methode wird von Java aufgerufen, wenn eine Taste 
	 * auf dem Keyboard gedrueckt wird (und das Spielefenster aktiv ist).
	 * Damit koennen wir also die Richtung der Schlangenbewegung aendern. 
	 */
	public void keyPressed(KeyEvent ke) {
		switch(ke.getKeyCode()){
		case 38:
			if(lastSnakeDirection != 2)
				snakeDirection = 0;
			break;
		case 39: 
			if(lastSnakeDirection != 3)
				snakeDirection = 1;
			break; 
		case 40:
			if(lastSnakeDirection != 0)
				snakeDirection = 2;
			break; 
		case 37:
			if(lastSnakeDirection != 1)
				snakeDirection = 3;
			break;
		default: 
			System.out.println("Key Pressed: "+ke.getKeyCode()+" "+ke);			
		}
	}

	/**
	 * Brauchen wir nicht zu benutzen, muss aber wegen dem KeyListener 
	 * implementiert werden. Bleibt hier also leer. 
	 */
	public void keyReleased(KeyEvent ke) {}

	/**
	 * Brauchen wir nicht zu benutzen, muss aber wegen dem KeyListener 
	 * implementiert werden. Bleibt hier also leer. 
	 */
	public void keyTyped(KeyEvent ke) {}

}