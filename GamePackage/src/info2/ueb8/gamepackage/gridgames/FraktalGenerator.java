package info2.ueb8.gamepackage.gridgames;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import info2.ueb8.gamepackage.GameScores;
import info2.ueb8.gamepackage.GridGameInterface;
import info2.ueb8.gamepackage.GridGameTools;

public class FraktalGenerator implements GridGameInterface, MouseListener {
	
		/**
		 *  Warteschlange, um die richtige Reihenfolge der erscheinenden Fraktale zu ermitteln
		 */
		private Queue<SimpleEntry<int[], Rectangle>> queue;
		
		/**
		 *  Array der weißen Quadrate, also wo der Wert -1 ist
		 */
		public Rectangle[] whiteRectangles;
		
		/**
		 *  Startquadrat 
		 */
		public Rectangle startRectangle;
		
		/**
		 *  erster Code Schlüssel 
		 */
		public int[] startKey;
	
	    int sleepTime = 50;

	    private Rectangle gameArea;
	    private Rectangle controlArea;

	    private JPanel myPanel;

	    /**
	     * width=height of a tile
	     */
	    public static int initTileSize = 1;

	    private boolean isRunning = false;

	    public FraktalGenerator(JPanel yourPanel, Rectangle gameArea, Rectangle controlArea) {
	    	
	        this.gameArea = new Rectangle(gameArea);
	        // setze Fenstergröße auf 512, da 2er Potenz
	        this.gameArea.width = 512;
	        this.gameArea.height = 512;
	        
	        this.controlArea = new Rectangle(controlArea);

	        myPanel = yourPanel;

	        myPanel.addMouseListener(this);

	        resetGame();
	    }

	    private void resetGame() {
	    	
	        whiteRectangles = new Rectangle[]{};
	        startRectangle = gameArea;
	        startKey = new int[4];
	        
	        // fügt der Liste Key, den Wert -1 und 3 Random Werte von 1-8 zu
	        List<Integer> key = new ArrayList<Integer>();
	        key.add(-1);
	        
	        for(int i = 1; i < startKey.length; i++) {
	        	key.add(ThreadLocalRandom.current().nextInt(8) + 1);
	        }
	        
	        // mischt die Werte in Key
	        Collections.shuffle(key);
	        
	        // ordnet dem Code Schlüssel cy,c2,c3 und c4 Werte zu
	        for(int i = 0; i < startKey.length; i++) {
	        	startKey[i] = key.get(i);
	        }
	        
	        queue = new LinkedList<SimpleEntry<int[], Rectangle>>();
	        
	        isRunning = false;
	        
	    }

	    @Override
	    public GameScores getScores() {
	        return GameScores.createGameScores("Fractal Generator", 0);
	    }

	    @Override
	    public void closeGame() {
	        isRunning = false;
	        myPanel.removeMouseListener(this);
	    }

	    /**
	     * Paints the fractals
	     */
	    public void paintGameArea(Graphics2D g) {
	    	// färbt das gesamte Fenster schwarz ein
	    	g.setColor(Color.BLACK);
	    	g.fillRect(startRectangle.x, startRectangle.y, startRectangle.width, startRectangle.height);
	    	
	    	// färbt an den Positionen von whiteRectangles die Flächen weiß
	    	g.setColor(Color.WHITE);
	        for(int i = 0; i < whiteRectangles.length; i++) {
	        	g.fillRect(whiteRectangles[i].x, whiteRectangles[i].y, whiteRectangles[i].width, whiteRectangles[i].height);
	        }
	    }

	    public void paintControlArea(Graphics2D g) {
	        g.setColor(Color.black);
	        g.drawRect(controlArea.x, controlArea.y, controlArea.width, controlArea.height);
	        if (!isRunning)
	            g.drawString("Start generate", controlArea.x + 5,
	                controlArea.y + controlArea.height / 2 + g.getFont().getSize() / 2);
	        else
	            g.drawString("Reset generation", controlArea.x + 5,
	                controlArea.y + controlArea.height / 2 + g.getFont().getSize() / 2);
	    }

	    @Override
	    public String getName() {
	        return "The Fractal Generator";
	    }

	    @Override
	    public void mouseClicked(MouseEvent arg0) {
	    }

	    @Override
	    public void mouseEntered(MouseEvent arg0) {
	    }

	    @Override
	    public void mouseExited(MouseEvent arg0) {
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	        int mouseX = e.getX();
	        int mouseY = e.getY();
	        if (GridGameTools.containedInWhichArea(mouseX, mouseY, controlArea) == 0) {
	            if (isRunning) {
	                resetGame();
	            } else {
	                resetGame();
	                isRunning = true;
	                (new Thread(new FractalThread())).start();
	            }
	        }

	    }

	    public void mouseReleased(MouseEvent arg0) {
	    }

	    private class FractalThread implements Runnable {
	    	
	        public void run() {
	        	
	        	// initialisiere das Spiel mit dem ersten Code-Schlüssel und dem erstem Quadrat
	            makeFractals(startKey, startRectangle);
	            
	            // solange sich Quadrate in der Warteschlange befinden, 
	            // sollen die Keys und Quadrate aus der Warteschlange gezogen werden
	            while (queue.peek() != null) {
	            	SimpleEntry<int[], Rectangle> var = queue.poll();
	            	makeFractals(var.getKey(), var.getValue());
	            	
	            	myPanel.repaint();
		        	
		        	try {
		                Thread.sleep(sleepTime);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        	
	            }
	            isRunning = false;
	            myPanel.repaint();
	        }

	        /**
	         * generiert die Fraktale im Quadrat rectangle mit dem Code-Schlüssel aus c
	         */
	        private void makeFractals(int[] c, Rectangle rectangle) {
	        	
	        	Rectangle[] quaters = splitRectangle(rectangle);
	        	Rectangle[] newWhiteRectangles = new Rectangle[whiteRectangles.length + 1];
	        	System.arraycopy(whiteRectangles, 0, newWhiteRectangles, 0, whiteRectangles.length);
	        	
	        	// ermitteln, welches c den Wert -1 hat
	        	// wenn Wert -1 hinzufuegen zum Array 
	        	if (c[0] < 0) {
	        		newWhiteRectangles[newWhiteRectangles.length-1] = quaters[0];
	        	}
	        	if (c[1] < 0) {
	        		newWhiteRectangles[newWhiteRectangles.length-1] = quaters[1];
	        	}
	        	if (c[2] < 0) {
	        		newWhiteRectangles[newWhiteRectangles.length-1] = quaters[2];
	        	}
	        	if (c[3] < 0) {
	        		newWhiteRectangles[newWhiteRectangles.length-1] = quaters[3];
	        	}
	        	
	        	whiteRectangles = newWhiteRectangles;
	        	
	        	
	        	for(int i = 0; i < quaters.length; i++) {
	        		if (!isRunning) 
	        			return;
	        		if (quaters[i].width < 2 || c[i] < 0) 
	        			continue;
	        		
	        		int[] key = shuffleKey(c[0], c[1], c[2], c[3], c[i]);
	        		
	        		queue.offer(new SimpleEntry<int[], Rectangle>(key, quaters[i]));
	        	}
	        	
	        }
	        
	        /**
	         *   teilt das Quadrat in 4 gleiche Teile auf
	         **/
	        private Rectangle[] splitRectangle(Rectangle rectangle) {
	        	Rectangle topleft = new Rectangle();
	        	Rectangle topright = new Rectangle();
	        	Rectangle botleft = new Rectangle();
	        	Rectangle botright = new Rectangle();
	        	
	        	int halfwidth = rectangle.width/2;
	        	int halfheight = rectangle.height/2;
	     
	        	topleft.setRect(rectangle.x, rectangle.y, halfwidth, halfheight);
	        	topright.setRect(rectangle.x + halfwidth, rectangle.y, halfwidth, halfheight);
	        	botleft.setRect(rectangle.x, rectangle.y + halfheight, halfwidth, halfheight);
	        	botright.setRect(rectangle.x + halfwidth, rectangle.y + halfheight, halfwidth, halfheight);
	        	
	        	return new Rectangle[] {topleft, botleft, botright, topright};
	        }
	        
	        /**
	         *   Tabelle auf dem Blatt
	         *   rekursiver Code Schlüssel
	         **/
	        private int[] shuffleKey(int c1, int c2, int c3, int c4, int key) {
	        	int[] result;
	        	switch (key) {
	        	case 1:
	        		result = new int[]{c1, c2, c3, c4};
	        		break;
	        	case 2:
	        		result = new int[]{c2, c3, c4, c1};
	        		break;
	        	case 3:
	        		result = new int[]{c3, c4, c1, c2};
	        		break;
	        	case 4:
	        		result = new int[]{c4, c1, c2, c3};
	        		break;
	        	case 5:
	        		result = new int[]{c1, c4, c3, c2};
	        		break;
	        	case 6:
	        		result = new int[]{c2, c1, c4, c3};
	        		break;
	        	case 7:
	        		result = new int[]{c3, c2, c1, c4};
	        		break;
	        	case 8:
	        		result = new int[]{c4, c3, c2, c1};
	        		break;
	        	default:
	        		result = null;
	        	}
	        	return result;
	        }

	    }
	}



