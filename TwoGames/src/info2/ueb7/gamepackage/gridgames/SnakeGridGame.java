package info2.ueb7.gamepackage.gridgames;

// Die folgenden imports sind notwendig, um die Grafik zu unterstuetzen.
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import info2.ueb7.gamepackage.*;

/**
 * 
 * @author Martin V. Butz 11 / 2013
 */
public class SnakeGridGame implements GridGameInterface, ActionListener {
    /**
     * The colors that are supported by the game.
     */
    public static Color snakeHead = new Color(250, 10, 0); // rot
    public static Color snakeBody = new Color(128, 0, 0); // dunkelrot
    public static Color bgColor = new Color(0, 150, 230); // hellblau
    public static Color foodColor = new Color(0, 128, 0); // dunkelgruen
    public static Color goldFoodColor = new Color(255, 215, 0); // gold
    public static Color borderColor = new Color(0, 0, 0); // schwarz

    /**
     * The timer controls the game speed by iteratively invoking action events -
     * which in turn result in the execution of one step of the snake.
     */
    private Timer timer;

    /**
     * The game area in Rectangle format.
     */
    private Rectangle gameArea;

    /**
     * The control area - where the speed should be shown.
     */
    private Rectangle controlArea;

    /**
     * area within the game area where the snake "lives"
     */
    private Rectangle snakeArea;

    /**
     * The panel in which all this happens.
     */
    private JPanel myPanel;

    /**
     * true if the snake is still "alive"
     */
    private boolean stillAlive;

    /**
     * initial width=height of a tile
     */
    public static int initTileSize = 20;

    /**
     * the actual current width=height of a tile
     */
    private int tileSize;

    /**
     * Number of Grid rows of the grid game area.
     */
    public int numRows;

    /**
     * Number of grid columns of the grid game area.
     */
    public int numColumns;

    /**
     * The Locations of the Snake (in the grid)
     */
    private int[][] snakeLoc;

    /**
     * The current length of snake (in number of grid cells occupied)
     */
    private int snakeLength;

    /**
     * The index of the snake's head in the array.
     */
    private int snakeIndex = 0;

    /**
     * The direction towards which the snake is heading. Note: the snake is
     * always moving! Direction Code: 0 := North; 1 := East; 2 := South; 3 :=
     * West
     */
    private int snakeDirection = 0;

    /**
     * The last snake direction encodes the previous movement before a turn. It
     * is used to prevent movements backwards into the snake's own body.
     */
    private int lastSnakeDirection = snakeDirection;

    /**
     * The Location of the food item (cell location)
     */
    private int[] foodLoc;

    /**
     * true if currently a golden foot item is out there.
     */
    private boolean foodIsGold = false;

    /**
     * probability of occurrence of a golden food item.
     */
    private final double FOOD_GOLD_PROB = .8;

    /**
     * Number of iterations played in the game.
     */
    public int iteration = 0;

    /**
     * Number of food items that were eaten.
     */
    private int foodEaten = 0;

    /**
     * Number of foot items to win.
     */
    private final int FOOD_ITEM_GOAL = 30;

    /**
     * The frequency of a move - every gameSpeed Milliseconds, the snake moves a
     * step.
     */
    private int gameSpeed = 150;

    // TODO you may want to add a level attribute here.

    /**
     * The snake key adapter that monitors for key events.
     */
    private SnakeKeyAdapter mySKA;

    /**
     * Reference to the frame in which the game is played.
     */
    private JFrame inFrame;

    /**
     * Constructor for this game. Constructs the game and starts it immediately.
     * 
     * @param inFrame
     *            Reference to the frame within this game is played. Needed to
     *            register key events and to be able to remove the key listener
     *            again at the end of the game.
     * @param yourPanel
     *            the panle within the game is played. Needed to invoke
     *            repaint()
     * @param gameArea
     *            The game area within the panel.
     * @param controlArea
     *            The control area within the panel.
     */
    public SnakeGridGame(JFrame inFrame, JPanel yourPanel, Rectangle gameArea, Rectangle controlArea) {
        this.gameArea = new Rectangle(gameArea);
        this.controlArea = new Rectangle(controlArea);
        myPanel = yourPanel;

        tileSize = initTileSize;
        initFullSnakeGamePanel();

        this.inFrame = inFrame;
        mySKA = new SnakeKeyAdapter();
        inFrame.addKeyListener(mySKA);
        inFrame.setFocusable(true); // from now on the board has the keyboard
                                    // input, explicit call is needed!
    }

    /**
     * Closes the game by stopping the time and removing the key listener to the
     * game frame.
     */
    public void closeGame() {
        timer.stop();
        timer = null;
        inFrame.removeKeyListener(mySKA);
        mySKA = null;
        inFrame = null;
    }

    /**
     * Initializes the game - especially the grid area, the current snake
     * location, its body, resets the points and iteration counters and
     * activates the time to start the snake.
     */
    private void initFullSnakeGamePanel() {
        // Reihen = Anzahl Reihen minus zwei fuer den Rand
        // Vorsicht: Reihen bestimmen die Hoehe des Feldes NICHT die Breite!!!
        numRows = gameArea.height / tileSize - 2;
        // Spalten = Anzahl Spalten minus zwei fuer den Rand
        numColumns = gameArea.width / tileSize - 2;

        snakeArea = new Rectangle(gameArea.x + tileSize, gameArea.y + tileSize, gameArea.width - tileSize * 2,
            gameArea.height - tileSize * 2);

        snakeLength = 10;
        snakeLoc = new int[snakeLength][2];
        snakeIndex = snakeLength - 1;
        for (int i = 0; i <= snakeIndex; i++) {
            snakeLoc[i][0] = numColumns / 2;
            snakeLoc[i][1] = numRows / 2;
        }

        foodLoc = new int[2];
        resetFoodLocation();

        iteration = 0;
        snakeDirection = 0;
        foodEaten = 0;

        stillAlive = true;

        timer = new Timer(gameSpeed, this); // timer is calling action events
                                            // every 400ms, in this case
                                            // actionPerformed() method
        timer.start();
    }

    /**
     * A game iteration in which the snakes moves forward in its current heading
     * direction one step. Invoked currently by an action event, which is
     * periodically invoked when the timer is active.
     */
    private void nextStep() {
        if (stillAlive && foodEaten < FOOD_ITEM_GOAL) {
            // probe if the next step is valid or not
            int x = snakeLoc[snakeIndex][0];
            int y = snakeLoc[snakeIndex][1];
            switch (snakeDirection) {
            case 0: // North
                y--;
                if (y < 0 || inSnake(x, y))
                    stillAlive = false;
                break;
            case 1: // EAST
                x++;
                if (x >= numColumns || inSnake(x, y))
                    stillAlive = false;
                break;
            case 2: // SOUTH
                y++;
                if (y >= numRows || inSnake(x, y))
                    stillAlive = false;
                break;
            case 3: // WEST
                x--;
                if (x < 0 || inSnake(x, y))
                    stillAlive = false;
                break;
            }
            snakeIndex = (snakeIndex + 1) % snakeLoc.length;
            // Move the snake.
            snakeLoc[snakeIndex][0] = x;
            snakeLoc[snakeIndex][1] = y;
            // Remember the motion direction to prevent moving backwards.
            lastSnakeDirection = snakeDirection;

            if (snakeLoc[snakeIndex][0] == foodLoc[0] && snakeLoc[snakeIndex][1] == foodLoc[1]) {
                // at food location --- am eating food.
                if (foodIsGold) {
                    foodEaten += 10;
                    snakeLength += 10;
                    foodIsGold = false;
                } else {
                    foodEaten++;
                    snakeLength++;
                }
                expandSnake(snakeLength);
                if (Math.random() < FOOD_GOLD_PROB)
                    foodIsGold = true;
                resetFoodLocation();
            }
            iteration++;
            myPanel.repaint();
        } else {
            // game / level done - won or proceed to the next level / or show
            // end-game dialog
            timer.stop();
            // TODO here the next level may be started (you will also need to
            // modify the game re-initalization cf. initFullSnakeGamePanel() )
            if (stillAlive) {
                JOptionPane.showMessageDialog(myPanel,
                    "Congratulations - You won the Game!!!\n" + "After " + iteration
                        + " steps you have reached the maximum \n" + "snake length by eating " + FOOD_ITEM_GOAL
                        + " items.");
            } else {
                JOptionPane.showMessageDialog(myPanel,
                    "Ouch - that hurt :-(\n" + "You stayed alive for " + iteration + " steps\n" + "and have eaten "
                        + foodEaten + " itmes during that time.\n" + "T R Y    A G A I N   !!!");
            }
            initFullSnakeGamePanel();
            myPanel.repaint();
        }
    }

    /**
     * Reset the food location to a new position - not in the snake.
     */
    private void resetFoodLocation() {
        boolean done = false;

        while (!done) {
            foodLoc[0] = (int) (Math.random() * numColumns);
            foodLoc[1] = (int) (Math.random() * numRows);
            // do not position the food into the snake
            if (!inSnake(foodLoc[0], foodLoc[1]))
                done = true;
            // TODO - still need to make sure that the food is not put into a
            // barrier.
        }
    }

    /**
     * 
     * 
     * @param x
     *            location in the grid.
     * @param y
     *            location in the grid.
     * @return true when the specified cell is occupied by the snake currently.
     */
    private boolean inSnake(int x, int y) {
        for (int i = 0; i < snakeLength; i++) {
            if (x == snakeLoc[i][0] && y == snakeLoc[i][1])
                return true;
        }
        return false;
    }

    /**
     * Expands the snake by the specified number of segments. Initializes the
     * locations of the new segments to the tail of the snake.
     * 
     * @param oldSnake
     * @param snakeIndex
     *            index of the head of the snake in the oldSnake Array.
     * @param newSnakeLength
     *            the new Snake Length
     * @return the new snake
     */
    private void expandSnake(int newSnakeLength) {
        int[][] newSnake = new int[newSnakeLength][2];
        int numSegmentsToAdd = newSnakeLength - snakeLoc.length;
        int snakeTail = (snakeIndex + 1) % snakeLoc.length;
        for (int i = 0; i < newSnake.length; i++) {
            if (i <= snakeIndex) {
                newSnake[i][0] = snakeLoc[i][0];
                newSnake[i][1] = snakeLoc[i][1];
            } else if (i <= snakeIndex + numSegmentsToAdd) {
                newSnake[i][0] = snakeLoc[snakeTail][0];
                newSnake[i][1] = snakeLoc[snakeTail][1];
            } else {
                newSnake[i][0] = snakeLoc[i - numSegmentsToAdd][0];
                newSnake[i][1] = snakeLoc[i - numSegmentsToAdd][1];
            }
        }
        snakeLoc = newSnake;
    }

    /**
     * Creates a game score object that reflects the current score.
     */
    public GameScores getScores() {
        return GameScores.createGameScores(new String[] { "Snake Length", "Points Eaten" },
            new int[] { snakeLength, foodEaten });
    }

    /**
     * Returns the name of the game.
     */
    public String getName() {
        return "Snake";
    }

    /**
     * Action Performed is invoked by the Timer object.
     */
    public void actionPerformed(ActionEvent arg0) {
        nextStep();
    }

    /*
     * #########################################################################
     * ######################### ################## Methods to Paint the Board
     * including all three areas ########################## ########## -> fuer
     * die Lesbarkeit des Codes durch dieses Kommentar etwas abgetrennt <-
     * ###########
     * #########################################################################
     * #########################
     */

    /**
     * This method paints the game area and the border.
     * 
     * @param g
     *            The graphics object to paint in.
     */
    public void paintGameArea(Graphics2D g) {
        g.setColor(borderColor);
        g.fillRect(gameArea.x, gameArea.y, gameArea.width, gameArea.height);

        g.setColor(bgColor);
        g.fillRect(snakeArea.x, snakeArea.y, snakeArea.width, snakeArea.height);

        // TODO Paint additional barriers here

        paintFood(g);
        paintSnake(g);
    }

    /**
     * This methods paints the food item to the graphics object.
     * 
     * @param g
     *            The graphics object to paint in.
     */
    public void paintFood(Graphics2D g) {
        if (foodIsGold)
            g.setColor(goldFoodColor);
        else
            g.setColor(foodColor);
        g.fillRect(snakeArea.x + foodLoc[0] * tileSize, snakeArea.y + foodLoc[1] * tileSize, tileSize, tileSize);
    }

    /**
     * This methods paints the snake into the game area at its current location.
     * 
     * @param g
     *            The graphics object to paint in.
     */
    public void paintSnake(Graphics g) {
        g.setColor(snakeBody);
        for (int i = 0; i < snakeLength; i++) { // paint the whole body
                                                // including the head.
            g.fillRect(snakeArea.x + snakeLoc[i][0] * tileSize, snakeArea.y + snakeLoc[i][1] * tileSize, tileSize,
                tileSize);
        }
        // now repaing the head in the proper color.
        g.setColor(snakeHead);
        g.fillRect(snakeArea.x + snakeLoc[snakeIndex][0] * tileSize, snakeArea.y + snakeLoc[snakeIndex][1] * tileSize,
            tileSize, tileSize);
    }

    public void paintControlArea(Graphics2D g) {

        // TODO Paint the current game speed into the game control area.
        // TODO Paint the current level into the game control area

    }

    /*
     * #########################################################################
     * ######################### ####################### Methods to handle the
     * relevant key presses ############################### ########## -> fuer
     * die Lesbarkeit des Codes durch dieses Kommentar etwas abgetrennt <-
     * ###########
     * #########################################################################
     * #########################
     */

    /**
     * Inner class SnakeKeyAdaper is used to extend the KeyAdapter for handling
     * key presses. It overwrites the abstract Method keyPressed(KeyEvent e).
     * The Method is called when a key is pressed and the frame, within which
     * the game is played, is currently listening to the game (via this
     * KeyListener).
     */
    class SnakeKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (lastSnakeDirection != 2)
                    snakeDirection = 0;
                break;
            case KeyEvent.VK_RIGHT:
                if (lastSnakeDirection != 3)
                    snakeDirection = 1;
                break;
            case KeyEvent.VK_DOWN:
                if (lastSnakeDirection != 0)
                    snakeDirection = 2;
                break;
            case KeyEvent.VK_LEFT:
                if (lastSnakeDirection != 1)
                    snakeDirection = 3;
                break;
            // TODO Handle plus = KeyEvent.VK_PLUS/ minus = KeyEvent.VK_MINUS
            // KeyPresses
            default:
                System.out.println("Key Pressed: " + e + " " + e.getKeyCode());
            }
        }
    }

}