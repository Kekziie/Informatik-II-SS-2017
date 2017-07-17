package info2.ueb8.gamepackage.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import info2.ueb8.gamepackage.GridGameInterface;
import info2.ueb8.gamepackage.gridgames.ColorAreaGridGame;
import info2.ueb8.gamepackage.gridgames.FraktalGenerator;
import info2.ueb8.gamepackage.gridgames.Minesweeper;
import info2.ueb8.gamepackage.gridgames.SnakeGridGame;
import info2.ueb8.gamepackage.gridgames.TowersOfHanoi;


/**
 * The main game frame hosts the Games that implement the GridGameInterface. All
 * Implemented Games should be added to the enum ImplementedGames. Implements
 * the ActionListener Interface in order to process menu events.
 * 
 * @author Martin V. Butz 12/2013 & 01/2014
 */
public class MainGameFrame implements ActionListener {

	/**
	 * The available implemented Games. Add a new Game here if you implemented
	 * one!
	 */
	private enum ImplementedGames {
		ColorAreaGridGame, SnakeGridGame, Minesweeper
	};

	/**
	 * The available implemented Games. Add a new Game here if you implemented
	 * one!
	 */
	private enum ImplementedAlgs {
		TowersOfHanoi, FraktalGenerator
	};

	/**
	 * One game is inevitably the one the whole frame starts with.
	 */
	public static ImplementedGames startupGame = ImplementedGames.ColorAreaGridGame;

	/**
	 * The Window JFrame in which the game is placed and in which also the menu
	 * is hosted.
	 */
	private JFrame gameFrame;

	/**
	 * Frame width upon creation.
	 */
	public int initFrameWidth = 635;

	/**
	 * Frame height upon creation.
	 */
	public int initFrameHeight = 650;

	/**
	 * The panel within which a game is hosted.
	 */
	private MyPanel thePanel;

	/**
	 * Width of the panel for the game.
	 */
	private int panelWidth;

	/**
	 * Height of the panel for the game.
	 */
	private int panelHeight;

	/**
	 * Left-Top Area - in which the games can add game control stuff.
	 */
	private Rectangle controlArea;

	/**
	 * Right-Top Area - in which the current score is displayed.
	 */
	private Rectangle scoreArea;

	/**
	 * Game Area - in which the actual game is shown.
	 */
	private Rectangle gameArea;

	/**
	 * Reference to the current game that is currently displayed and played.
	 */
	private GridGameInterface myCurrentGame;

	/**
	 * The main game frame - with which the initial game is created.
	 */
	public MainGameFrame() {

		// create first an empty JFrame
		gameFrame = new JFrame("My Game Frame");
		// ensure that all processes are stopped.
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(initFrameWidth, initFrameHeight);
		gameFrame.setLocationRelativeTo(null); // center the frame
		gameFrame.setResizable(false); // we do not allow resizing.

		// create the MenuBar with all the games
		JMenuBar jmb = new JMenuBar();
		// Build the GameChoice menu.
		JMenu menu = new JMenu("Game Choice");
		menu.setMnemonic(KeyEvent.VK_G);
		menu.setToolTipText("Here you can choose another game to play.");
		// a group of JMenuItems
		int numGames = 0;
		for (ImplementedGames ig : ImplementedGames.values()) {
			JMenuItem menuItem = new JMenuItem("" + ig, KeyEvent.VK_0 + numGames);
			numGames++;
			menuItem.getAccessibleContext().setAccessibleDescription("Start the " + ig + " game.");
			menuItem.addActionListener(this);
			menu.add(menuItem);
		}
		// now add the menu to the JMenuBar and the JMenuBar to the JFrame
		jmb.add(menu);
		// Build the Algorithms menu.
		menu = new JMenu("Alg. Choice");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.setToolTipText("Here you can choose a different algorithm.");
		// a group of JMenuItems
		for (ImplementedAlgs ialg : ImplementedAlgs.values()) {
			JMenuItem menuItem = new JMenuItem("" + ialg, KeyEvent.VK_0 + numGames);
			numGames++;
			menuItem.getAccessibleContext().setAccessibleDescription("Start the " + ialg + " algorithm.");
			menuItem.addActionListener(this);
			menu.add(menuItem);
		}
		// now add the menu to the JMenuBar and the JMenuBar to the JFrame
		jmb.add(menu);
		gameFrame.setJMenuBar(jmb);
		gameFrame.setVisible(true);
		gameFrame.validate();

		// Determine size of the panel area (the size remaining to display the
		// game)
		panelWidth = gameFrame.getContentPane().getWidth();
		panelHeight = gameFrame.getContentPane().getHeight();

		// start the game now, that is, initialize the game and add it to the
		// game panel.
		initGame(startupGame, null);
	}

	/**
	 * Starts the specified game in a new panel and adds this panel to the
	 * JFrame.
	 * 
	 * @param whichGame
	 *            The game that is going to be played (null if an alg should be
	 *            visualized).
	 * @param whichAlg
	 *            The game that is going to be visualized (null if a game should
	 *            be played)
	 */
	private void initGame(ImplementedGames whichGame, ImplementedAlgs whichAlg) {
		if (thePanel != null) { // remove the old panel if one was already
			// created.
			gameFrame.remove(thePanel);
		}
		// now create a new panel and initialize the game within it.
		thePanel = new MainGameFrame.MyPanel();

		if (whichGame != null) {
			switch (whichGame) {
			case ColorAreaGridGame:
				initFullGamePanel(panelWidth, panelHeight, ColorAreaGridGame.initTileSize,
						ColorAreaGridGame.initTileSize);
				myCurrentGame = new ColorAreaGridGame(thePanel, gameArea, controlArea);
				break;

			case SnakeGridGame:
				initFullGamePanel(panelWidth, panelHeight, SnakeGridGame.initTileSize, SnakeGridGame.initTileSize);
				myCurrentGame = new SnakeGridGame(gameFrame, thePanel, gameArea, controlArea);
				break;
			case Minesweeper:
				initFullGamePanel(panelWidth, panelHeight, Minesweeper.TileSize, Minesweeper.TileSize);
				myCurrentGame = new Minesweeper(thePanel, gameArea, controlArea);
				break;	
			}
		} else {
			switch (whichAlg) {
			case TowersOfHanoi:
				initFullGamePanel(panelWidth, panelHeight, ColorAreaGridGame.initTileSize,
						ColorAreaGridGame.initTileSize);
				myCurrentGame = new TowersOfHanoi(thePanel, gameArea, controlArea);
				break;	
			case FraktalGenerator:
				initFullGamePanel(panelWidth, panelHeight, ColorAreaGridGame.initTileSize,
						ColorAreaGridGame.initTileSize);
				myCurrentGame = new FraktalGenerator(thePanel, gameArea, controlArea);
				break;
			}
		}
		gameFrame.setTitle(myCurrentGame.getName());
		gameFrame.add(thePanel);
		gameFrame.validate();
	}

	/**
	 * Initializes the three areas that this class hosts. controlArea and
	 * gameArea should be filled with contents by the implementing game.
	 * scoreArea is filled by ScoreObjects.
	 * 
	 * @param width
	 *            The width of the panel, wherein the game-relevant areas are
	 *            placed.
	 * @param height
	 *            The height of the panel, wherein the game-relevant areas are
	 *            placed.
	 * @param widthConstraint
	 *            The width-constraint - typically the tile width - in order to
	 *            center the grid-game area.
	 * @param hightConstraint
	 *            The corresponding height-constraint.
	 */
	private void initFullGamePanel(int width, int height, int widthConstraint, int heightConstraint) {
		// initalization of color picker area (on the left top)
		controlArea = new Rectangle(5, 5, (2 * width / 3) - 10, 45);
		scoreArea = new Rectangle(5 + 2 * width / 3, 5, (width / 3) - 10, 45);
		int gameAreaWidth = ((width - 10) / widthConstraint) * widthConstraint;
		int gameAreaHeight = ((height - 60) / heightConstraint) * heightConstraint;
		gameArea = new Rectangle((width - gameAreaWidth) / 2, 55 + (height - 60 - gameAreaHeight) / 2, gameAreaWidth,
				gameAreaHeight);
	}

	/**
	 * Inner class to give the game panel the necessary additional features.
	 */
	private class MyPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1168947575194286689L;

		/**
		 * Draws the game board into this Panel within the frame. This method is
		 * called indirectly via java.awt by means of the repaint() Method.
		 */
		public void paintComponent(Graphics gg) {
			// first paint the default JPanel appearance.
			super.paintComponent(gg);
			// Cast to Graphics2D - higher flexibility in the graphics.
			Graphics2D g = (Graphics2D) gg;
			// Now draw the defaults background color - here WHITE.
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, panelWidth, panelHeight);

			// paint the current color field
			myCurrentGame.paintGameArea(g);

			// paint color picker area
			myCurrentGame.paintControlArea(g);

			// paint the game control and score area
			myCurrentGame.getScores().paintScore(g, scoreArea);
		}
	}

	/**
	 * The implemented Method from the action listener ensures that Menu
	 * activities are handled properly.
	 */
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		for (ImplementedGames ig : ImplementedGames.values()) {
			if (ig.toString().equals(cmd)) {
				myCurrentGame.closeGame();
				initGame(ig, null); // this call removes the old panel and adds
				// the new one.
				return;
			}
		}
		for (ImplementedAlgs ialg : ImplementedAlgs.values()) {
			if (ialg.toString().equals(cmd)) {
				myCurrentGame.closeGame();
				initGame(null, ialg); // this call removes the old panel and
				// adds the new one.
				return;
			}
		}
	}

}
