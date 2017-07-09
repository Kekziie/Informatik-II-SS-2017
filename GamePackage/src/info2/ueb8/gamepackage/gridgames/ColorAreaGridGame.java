package info2.ueb8.gamepackage.gridgames;

// Die folgenden imports sind notwendig, um die Grafik 
// und die Fensteranzeigen zu unterstuetzen.
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import info2.ueb8.gamepackage.GameScores;
import info2.ueb8.gamepackage.GridGameInterface;
import info2.ueb8.gamepackage.GridGameTools;

/**
 * Diese Klasse implementiert einfache Farb-Spiele basierend auf einer
 * Farbmatrix. Zusaetzlich implementiert die Klasse noch einen "MouseListener" -
 * der realisiert, dass die Klasse davon informiert wird, wenn die Mouse etwas
 * in dem Panel - naemlich in dem Spielbereich - macht. Wir nutzen insbesondere
 * die Funktionalitaet, dass die Methode "mouseClicked(...)" aufgerufen wird,
 * wenn man mit der Maus irgendwo in den Spielebereich klickt. ( Genaueres dazu
 * erst in spaeteren Vorlesungen. Wichtig ist dass die Deklaration der Klasse so
 * stehen bleibt.)
 * 
 * @author Simon Wegendt & Martin V. Butz 2013
 */
public class ColorAreaGridGame implements GridGameInterface, MouseListener {

    private Rectangle gameArea;
    private Rectangle controlArea;
    private JPanel myPanel;

    /**
     * width=height of a tile
     */
    public static int initTileSize = 35;

    /**
     * width=height of a tile
     */
    private int tileSize;

    /**
     * The matrix of tiles (each value in this 2D matrix encodes the color of a
     * tile) The matrix is encoded by rows x colums... Thus, myTiles[9][0], for
     * example, encodes the first tile in the 10th row; ... while myTiles[0,5],
     * for example, encodes the sixth tile in the first row.
     */
    private int[][] myTiles;

    /**
     * The matrix of current connected tiles (to the left top). Same size matrix
     * with rows x columns.
     */
    private boolean[][] connectedTiles;

    /**
     * Color that is currently selected.
     */
    public int selectedColor = -1;

    /**
     * Number of turns played in the game.
     */
    public int steps = 0;

    /**
     * True if the game is in a "winning" state. Typically, at the beginning of
     * a game, one has not won yet :-)
     */
    private boolean won = false;

    /*
     * #########################################################################
     * # ######################## ####################### Implementation of
     * Color Game ########################################### ########## -> fuer
     * die Lesbarkeit des Codes durch dieses Kommentar etwas abgetrennt <-
     * ###########
     * ##############################################################
     * ####################################
     */
    /**
     * Initializes the color game.
     * 
     * @param numRows
     *            the number of rows in the field.
     * @param numColumns
     *            the number of columns in the field.
     */
    public ColorAreaGridGame(JPanel yourPanel, Rectangle gameArea, Rectangle controlArea) {
        this.gameArea = new Rectangle(gameArea);
        this.controlArea = new Rectangle(controlArea);

        myPanel = yourPanel;

        tileSize = initTileSize;

        initGame();

        myPanel.addMouseListener(this);
    }

    public void closeGame() {
        myPanel.removeMouseListener(this);
    }

    private void initGame() {
        int numRows = gameArea.height / tileSize;
        int numColumns = gameArea.width / tileSize;

        myTiles = new int[numRows][numColumns];
        for (int x = 0; x < numRows; x++) {
            for (int y = 0; y < numColumns; y++) {
                myTiles[x][y] = (int) (Math.random() * GridGameTools.colors.length);
            }
        }
        connectedTiles = new boolean[myTiles.length][myTiles[0].length];
        connectedTiles[0][0] = true;
        steps = 0;
        selectedColor = -1;
        determineNewConnectedTiles();
    }

    /**
     * Determines the connected tiles (those that are connected by color to the
     * left top). Assumes that the boolean matrix is correctly set!
     * 
     * @return true if a new connected tile was detected (and added to the
     *         connect tile matrix)
     */
    private boolean determineNewConnectedTiles() {
        int connectedColor = myTiles[0][0];
        boolean done = false;
        boolean detectedNewConnectedTile = false;
        while (!done) {
            done = true;
            won = true;
            for (int x = 0; x < myTiles.length; x++)
                for (int y = 0; y < myTiles[0].length; y++)
                    if (connectedTiles[x][y] == false) {
                        if (myTiles[x][y] == connectedColor) {
                            if (connectedTiles[(x - 1 >= 0 ? x - 1 : 0)][y] == true
                                || connectedTiles[(x + 1 < myTiles.length ? x + 1 : x)][y] == true
                                || connectedTiles[x][(y - 1 >= 0 ? y - 1 : 0)] == true
                                || connectedTiles[x][(y + 1 < myTiles[0].length ? y + 1 : y)] == true) {
                                // OK - one connected tile belongs to the
                                // connected ones and
                                // this tile has the right color... thus, it is
                                // also connected :-)
                                connectedTiles[x][y] = true;
                                // have found another connected tile... so I am
                                // not done.
                                done = false;
                                detectedNewConnectedTile = true;
                            } else {
                                won = false; // because the tile could not be
                                // connected (no connected
                                // neighboring tile)
                            }
                        } else {
                            won = false; // because the tile is a different
                            // color
                        }
                    }
        }
        return detectedNewConnectedTile;
    }

    /**
     * This method changes the color of all tiles with the same color that are
     * connected to the top left tile.
     * 
     * @param colorCode
     *            the color that was chosen
     * @return true if game is won (i.e. uniform color field)
     */
    public boolean changeColor(int colorCode) {
        int colorTopleft = myTiles[0][0];

        if (colorTopleft == colorCode)
            return false; // we didn't click on a different color

        steps++;

        for (int x = 0; x < myTiles.length; x++)
            for (int y = 0; y < myTiles[0].length; y++)
                if (connectedTiles[x][y])
                    myTiles[x][y] = colorCode;

        determineNewConnectedTiles();

        return won;
    }

    /**
     * This method paints all the tiles to the graphics object.
     * 
     * @param g
     */
    public void paintGameArea(Graphics2D g) {
        GridGameTools.paintGridGameArea(g, gameArea, myTiles, tileSize);
    }

    /**
     * This method paints the color picker area with all colors that are
     * available.
     * 
     * @param g
     */
    public void paintControlArea(Graphics2D g) {
        GridGameTools.paintColorPickerArea(g, controlArea, selectedColor);
    }

    /*
     * #########################################################################
     * # ######################## ############################# Methods to
     * Handle Mouse Events ##################################### ########## ->
     * fuer die Lesbarkeit des Codes durch dieses Kommentar etwas abgetrennt <-
     * ###########
     * ##############################################################
     * ####################################
     */

    /**
     * This is called by the "Listener" to this JPanel when the mouse is clicked
     * within the frame, that is, this JPanel.
     * 
     * Also... auf gut Deutsch... wenn alles richtig initalisiert ist, dann kann
     * man in dieser Methode die Mausklicks, die innerhalb des GameBereichs
     * liegen, weiter verarbeiten.
     * 
     * @parm Der MouseEvent kodiert unter Anderem die Lokation des MouseClicks
     *       relativ zum Gamebereich.
     */
    public void mouseClicked(MouseEvent e) {
        // System.out.println("Mouse Event in Color Grid Game: "+e);
        // The MouseEvent Object can be used to determine the location of the
        // mouse click relative
        // to the panel boundaries (top-left location = 0,0)
        int mouseX = e.getX();
        int mouseY = e.getY();

        boolean won = false;
        // Determiniere zunaechst, welcher Bereich angeklickt wurde...
        // und verarbeite dann den Klick entsprechend weiter.
        if (GridGameTools.containedInWhichArea(mouseX, mouseY, controlArea) == 0) {
            // clicked within color picker area
            int x = mouseX - controlArea.x;
            selectedColor = (int) (((double) x / controlArea.width) * GridGameTools.colors.length);
            won = changeColor(selectedColor);
            myPanel.repaint();
        }
        if (won) {
            // Mit diesem sehr einfachen Befehl, wird bewirkt, dass sich ein
            // kleines Fenster oeffnet
            // in dem der spezifizierte Text dargestellt wird und das mit OK
            // wieder geschlossen werden kann.
            JOptionPane.showMessageDialog(myPanel,
                "Congratulations!\n" + "You won the Color Area Game\n in " + steps + " moves!");
            // Wenn man das Fenster dann geschlossen hat (durch Klick auf OK
            // Button), dann laeuft das Programm hier einfach weiter.

            // Re-initialisiere das aktuelle Spiel nach dem "Sieg".
            initGame();
            myPanel.repaint();
        }
    }

    /*
     * Hier folgen noch ein paar andere Methoden, die der MouseListener
     * benoetigt. Wir benutzen diese aber hier nicht. Deswegen sind die Methoden
     * einfach leer.
     */
    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public GameScores getScores() {
        return GameScores.createGameScores("Step", steps);
    }

    public String getName() {
        return "Growing Color-Area Game";
    }
}