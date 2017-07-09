package info2.ueb8.gamepackage.gridgames;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import info2.ueb8.gamepackage.GameScores;
import info2.ueb8.gamepackage.GridGameInterface;
import info2.ueb8.gamepackage.GridGameTools;

public class TowersOfHanoi implements GridGameInterface, MouseListener {

    int numberOfDiscs = 5;

    int sleepTime = 200;

    private int[][] discs;
    private int discFromLoc;
    private int discToLoc;

    private Rectangle gameArea;
    private Rectangle controlArea;

    private JPanel myPanel;

    /**
     * width=height of a tile
     */
    public static int initTileSize = 1;

    private boolean isRunning = false;

    private int movingFrom = 0;
    private int movingTo = 0;

    public TowersOfHanoi(JPanel yourPanel, Rectangle gameArea, Rectangle controlArea) {
        this.gameArea = new Rectangle(gameArea);
        this.controlArea = new Rectangle(controlArea);

        myPanel = yourPanel;

        myPanel.addMouseListener(this);

        resetGame();
    }

    private void resetGame() {
        discs = new int[3][numberOfDiscs];
        for (int i = 0; i < numberOfDiscs; i++)
            discs[0][i] = numberOfDiscs - i;
        isRunning = false;
        movingFrom = 0;
        movingTo = 0;
    }

    @Override
    public GameScores getScores() {
        return GameScores.createGameScores("Towers_Of_Hanoi", numberOfDiscs);
    }

    @Override
    public void closeGame() {
        isRunning = false;
        myPanel.removeMouseListener(this);
    }

    /**
     * Paints the discs.
     */
    public void paintGameArea(Graphics2D g) {
        double[] start = new double[3];
        start[0] = gameArea.x;
        start[1] = start[0] + gameArea.width / 3.;
        start[2] = start[1] + gameArea.width / 3.;
        double width = gameArea.width / 3;
        double decrement = width / (numberOfDiscs + 1);
        int heightStart = gameArea.y + gameArea.height;
        double heightInc = gameArea.height / (numberOfDiscs + 1);

        for (int i = 0; i < 3; i++) {
            // draw the pole
            GridGameTools.drawFilledRect(g, (int) (start[i] + width / 2 - 5),
                (int) (heightStart - (heightInc * (numberOfDiscs + .5))), 10, (int) (heightInc * (numberOfDiscs + .5)),
                GridGameTools.colors[i]);
            for (int j = 0; j < discs[i].length; j++) {
                if (discs[i][j] != 0) {
                    GridGameTools.drawFilledRect(g, (int) (start[i] + (decrement / 2) * (numberOfDiscs - discs[i][j])),
                        (int) (heightStart - heightInc * (j + 1)),
                        (int) (width - decrement * (numberOfDiscs - discs[i][j])), (int) heightInc, Color.BLUE);
                } else {
                    break;
                }
            }
        }
        // indicate move if one is prepared
        if (movingFrom > 0)
            // moving from indication.
            GridGameTools.drawFilledRect(g,
                (int) (start[movingFrom - 1] + (decrement / 2) * (numberOfDiscs - discs[movingFrom - 1][discFromLoc])),
                (int) (heightStart - heightInc * (discFromLoc + 1)),
                (int) (width - decrement * (numberOfDiscs - discs[movingFrom - 1][discFromLoc])), (int) heightInc,
                Color.GREEN);
        if (movingFrom > 0 && movingTo > 0) {
            // moving to indication
            GridGameTools.drawFilledRect(g,
                (int) (start[movingTo - 1] + (decrement / 2) * (numberOfDiscs - discs[movingFrom - 1][discFromLoc])),
                (int) (heightStart - heightInc * (discToLoc + 1)),
                (int) (width - decrement * (numberOfDiscs - discs[movingFrom - 1][discFromLoc])), (int) heightInc,
                Color.LIGHT_GRAY);
        }
    }

    public void paintControlArea(Graphics2D g) {
        g.setColor(Color.black);
        g.drawRect(controlArea.x, controlArea.y, controlArea.width, controlArea.height);
        if (!isRunning)
            g.drawString("Start Solution", controlArea.x + 5,
                controlArea.y + controlArea.height / 2 + g.getFont().getSize() / 2);
        else
            g.drawString("Reset Solution", controlArea.x + 5,
                controlArea.y + controlArea.height / 2 + g.getFont().getSize() / 2);
    }

    @Override
    public String getName() {
        return "The Towers of Hanoi";
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
                (new Thread(new HanoiThread())).start();
            }
        }

    }

    public void mouseReleased(MouseEvent arg0) {
    }

    private class HanoiThread implements Runnable {

        public void run() {
            moveTower(numberOfDiscs, 1, 3);
            isRunning = false;
            myPanel.repaint();
        }

        /**
         * The recursive method to move the discs. Assumes that all discs are
         * correctly stapled on the left when first called.
         * 
         * @param height
         * @param startPole
         * @param goalPole
         */
        private void moveTower(int height, int startPole, int goalPole) {
            if (height > 0 && isRunning) { // Rekursionsende bei height == 0
                int otherPole = 6 - startPole - goalPole;
                moveTower(height - 1, startPole, otherPole);
                if (!isRunning)
                    return;
                moveDisc(startPole, goalPole);
                if (!isRunning)
                    return;
                moveTower(height - 1, otherPole, goalPole);
            }
        }

        private void moveDisc(int startPole, int goalPole) {
            movingFrom = startPole;
            movingTo = goalPole;
            discFromLoc = 0;
            for (discFromLoc = 0; discFromLoc < discs[startPole - 1].length; discFromLoc++) {
                if (discs[startPole - 1][discFromLoc] == 0)
                    break;
            }
            if (discFromLoc == 0) {
                System.err.println("The disc moving did not work - the column is empty:-( ");
                return;
            }
            discFromLoc--;
            discToLoc = 0;
            for (discToLoc = 0; discToLoc < discs[goalPole - 1].length; discToLoc++) {
                if (discs[goalPole - 1][discToLoc] == 0)
                    break;
            }
            if (discToLoc == discs[goalPole - 1].length) {
                System.err.println("The disc moving did not work - no space on goal column!? :-( ");
                return;
            }
            myPanel.repaint();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            discs[goalPole - 1][discToLoc] = discs[startPole - 1][discFromLoc];
            discs[startPole - 1][discFromLoc] = 0;
            movingFrom = 0;
            movingTo = 0;
            myPanel.repaint();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
