package main;

// Die folgenden imports sind notwendig, um die Grafik zu unterstuetzen.
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
* @author Martin V. Butz 11 / 2013
*/

@SuppressWarnings("serial")

public class MySnakeGame extends JPanel implements KeyListener{
        
/**
* The colors that are supported by the game.
*/
        
        public static Color snakeHead     = new Color(250,10,0);    // rot
        public static Color snakeBody     = new Color(128,0,0);     // dunkelrot
        public static Color bgColor       = new Color(0,150,230);   // hellblau
        public static Color foodColor     = new Color(0,128,0);     // dunkelgruen
        public static Color goldFoodColor = new Color(255, 215, 0); // gold
        public static Color borderColor   = new Color(0, 0, 0);     // schwarz

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
        private static int[][] snakeLoc;
        
        // Laenge der Schlange
        private static int snakeLength=10;
        
        // Index der Schlange
        private static int snakeIndex=0;
        
        // maximale Länge der Schlange
        private static final int MAX_LENGTH=42;
        
/**
* The direction towards which the snake is heading.
* Note: the snake is always moving!
* Konvention hier: 0 := North (nach oben); 1 := East (nach rechts);
* 2 := South (nach unten); 3 := West (nach links)
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
        
        // ueberprueft, ob Essen vergoldet
        private static boolean FoodIsGold = false;
        
        // Wahrscheinlichkeit fuer Gold-Esspunkt
        private final static double GOLDFOOD = 0.25;
        
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
                
        // generieren des Fensters in dem Spiel stattfindet
        JFrame frame = new JFrame("My Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

// Der kleinere Spielbereich im gesamtem Fenster wird eingerichtet

        panelWidth = frame.getContentPane().getWidth();
        panelHeight = frame.getContentPane().getHeight();

// Spielbereich erstellen und ins Fenster einfuegen.

        mySnakeGame = MySnakeGame.initFullSnakeGamePanel();
        frame.add(mySnakeGame);

// Fenster soll auf Aktionen des Keyboards "hoeren"
// Fenster aktiviert, wenn man eine Taste drueckt
// die Methode (siehe deren Implementierung weiter unten) KeyTyped(...) aufgerufen.

        frame.addKeyListener(mySnakeGame);

        frame.validate();

// Spielstart

        runGame();

// Schliessen des Spiels beim Ende des Spiels

        frame.dispose();
}

/**
* Die Hauptschleife des Spiels 
*
* @throws InterruptedException
*/

private static void runGame() throws InterruptedException {
        
        // vermerkt Zusammenstoss 
        boolean stillAlive = true; 
        while(stillAlive && snakeLength < MAX_LENGTH) {
                
                // Schleife laeuft bis:
                // 1. ZusammenstoÃ?
                // 2. max. Laenge
                mySnakeGame.repaint();
                
                // Warten bis es Zeit ist, fuer den naechsten Schritt ist
                
                Thread.sleep(gameSpeed);
                
                // Bewegungsschritte der Schlange
                int x = snakeLoc[snakeIndex][0];
                int y = snakeLoc[snakeIndex][1];
                
                switch(snakeDirection){
                // Norden
                case 0: 
                        y--;
                        if(y < 0||insideSnake(x,y))
                        stillAlive = false;
                        break;
                // Osten        
                case 1: 
                        x++;
                        if(x >= numColumns||insideSnake(x,y))
                        stillAlive = false;
                         break;
                // Sueden
                case 2: 
                        y++;
                        if(y >= numRows||insideSnake(x,y))
                        stillAlive = false;
                        break;
                // Westen
                case 3: 
                        x--;
                        if(x < 0||insideSnake(x,y))
                        stillAlive = false;
                        break;
                        }
                
snakeIndex = (snakeIndex+1)%snakeLoc.length;

snakeLoc[snakeIndex][0] = x;
snakeLoc[snakeIndex][1] = y;

// durch Tastendruecke die naechste Richtung gewaehlt 

lastSnakeDirection = snakeDirection;
if(snakeLoc[snakeIndex][0]==foodLoc[0] &&
snakeLoc[snakeIndex][1]==foodLoc[1]) {
        
        if(FoodIsGold){
                if(snakeLength>=MAX_LENGTH){
                        
                        snakeLength=MAX_LENGTH;
                        FoodIsGold=false;
                        foodEaten+=10;
                        
                        
                } else {                        
                        snakeLength+=10;
                        snakeLoc = expandSnake(snakeLoc,snakeIndex,snakeLength);
                        foodEaten+=10;
                        FoodIsGold=false;
                 }
                } else {                        
                        foodEaten++;
                        snakeLength++;
                        snakeLoc = expandSnake(snakeLoc,snakeIndex,(snakeLength));
                        
                }
        if(Math.random() < GOLDFOOD)
                FoodIsGold=true;
                resetFoodLocation();
}

iteration++;

}
        
if(stillAlive && snakeLength>=MAX_LENGTH) {
        // Mittels diesem Befehl, wird ein kleines Fenster mit Text geoeffnet
        // Druecken auf OK Button schliesst das Fenster 
        // Programm laeuft weiter.
        JOptionPane.showMessageDialog(mySnakeGame, "Gewonnen !!!\n"
        + "Du hast "+iteration+" Schritte gebraucht um \n"
        + "alle moeglichen " + foodEaten + " Punkte zu fressen.");        
        } else {
                JOptionPane.showMessageDialog(mySnakeGame, "Sorry - das war's!\n"
                                + "Du hast "+iteration+" Schritte durchgehalten\n"
                                + "und dabei " + foodEaten + " Punkte gefressen.");
                }
}

private static int[][] expandSnake(int[][]oldSnake, int snakeIndex, int newSnakeLength){
        int newSegments = newSnakeLength - oldSnake.length;
        
        int[][] newSnake = new int[newSnakeLength][2];
        System.arraycopy(oldSnake, 0, newSnake, 0, snakeIndex + 1);
        
        for(int k = snakeIndex + 1; k < snakeIndex + newSegments + 1; k++) {
                newSnake[k] = oldSnake[snakeIndex].clone();
        }
        System.arraycopy(oldSnake, snakeIndex + 1, newSnake, snakeIndex + newSegments + 1, oldSnake.length - snakeIndex - 1);
                
    return newSnake;    
}


// Setzt Essen an neue Stelle.

private static void resetFoodLocation() {
        do {
        	//TODO Stelle sicher, dass das Essen nicht in die Schlange selber plaziert wird.
            foodLoc[0] = (int)(Math.random() * numColumns);
            foodLoc[1] = (int)(Math.random() * numRows);
        } while(insideSnake(foodLoc[0], foodLoc[1]));
        
        }

private static boolean insideSnake(int x, int y) {
        for(int i=0; i<snakeLength; i++) {
                if(x == snakeLoc[i][0] &&
                                y == snakeLoc[i][1])
                                return true;
        }
        return false;
}

/**
* Konstruktor fuer ein Graphik-Objekt.
* Benoetigen wir, um einen Key-Listener auf dieses JPanel kreieren zu koennen.
* Ist leer, aber ganz wichtig, denn durch das Kreieren, wird indirekt die ganze
* JPanel Funktionalitaet kreiert, weil ja diese Klasse JPanel "erweitert"...
* Wenn man das im Moment noch nicht so ganz versteht, dann ist das schon OK :-)
*/

private MySnakeGame() {}

// Hier wird der Spielbereich kreiert und als Objekt zurueckgeliefert.

public static MySnakeGame initFullSnakeGamePanel() {
        
        // Reihen = Anzahl Reihen - 2 Rand 
        // Vorsicht: Reihen bestimmen die Hoehe des Feldes NICHT die Breite
        numRows = panelHeight/TILE_SIZE -2;
        
        // Spalten = Anzahl Spalten - 2 Rand
        numColumns = panelWidth/TILE_SIZE -2;
        gameAreaWidth = numColumns * TILE_SIZE;
        gameAreaHeight = numRows * TILE_SIZE;
        gameAreaLeft = (panelWidth - gameAreaWidth) / 2;
        gameAreaTop = (panelHeight - gameAreaHeight) / 2;
        snakeLoc = new int[snakeLength][2];
        snakeIndex=snakeLength-1;
        
        for (int l=0; l<=snakeIndex;l++){
                snakeLoc[l][0] = numColumns/2;
                snakeLoc[l][1] = numRows/2;
                }
        foodLoc = new int[2];
        resetFoodLocation();
        iteration = 0;
        snakeDirection = 0;

// Spielbereich wird aufgerufen

MySnakeGame mySnakeG = new MySnakeGame();

// Spielbereichsobjekt zurueck gegeben 
return mySnakeG;

}

/**
* Draws the game board into this Panel within the frame.
*/

public void paintComponent(Graphics gg) {
        
        // gesamter Spielbereich wird "gemalt"
        super.paintComponent(gg);
        
        // Raender erstellt
        paintGameArea(gg);
        
        // Esspunkte erstellt
        paintFood(gg);
        
        // Schlange erstellt
        paintSnake(gg);
        }

/**
* This method paints the game area and the border.
*/

public static void paintGameArea(Graphics g) {
        g.setColor(borderColor);
        g.fillRect(0, 0, panelWidth, panelHeight);
        g.setColor(bgColor);
        g.fillRect(gameAreaLeft, gameAreaTop, gameAreaWidth, gameAreaHeight);
        }

/**
* This methods paints the food item to the graphics object.
*/

public static void paintFood(Graphics g) {
        if(FoodIsGold)
                g.setColor(goldFoodColor);
        else g.setColor(foodColor);
        g.fillRect(gameAreaLeft + foodLoc[0]*TILE_SIZE, gameAreaTop + foodLoc[1]*TILE_SIZE,
                        TILE_SIZE, TILE_SIZE);
        }

/**
* This methods paints the snake into the game area
* at its current location.
*/

public static void paintSnake(Graphics g) {
	
        // "anmalen" des Koerpers der Schlange
        g.setColor(snakeBody);
        for(int i=0; i<snakeLength; i++) {
        	
                g.fillRect(gameAreaLeft + snakeLoc[i][0]*TILE_SIZE,
                                gameAreaTop + snakeLoc[i][1]*TILE_SIZE,
                                TILE_SIZE, TILE_SIZE);                                
        }
        
        g.setColor(snakeHead);
        g.fillRect(gameAreaLeft + snakeLoc[snakeIndex][0]*TILE_SIZE, gameAreaTop +
        snakeLoc[snakeIndex][1]*TILE_SIZE,
        TILE_SIZE, TILE_SIZE);
        
}

/**
* Diese Methode wird von Java aufgerufen, 
* wenn eine Taste auf dem Keyboard gedrueckt wurde 
* (Spielfenster sollte dabei aktiv sein)
* Ã¤ndern der Richtung von der Schlange
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

// sollte leer bleiben

public void keyReleased(KeyEvent ke) {}

public void keyTyped(KeyEvent ke) {}

}