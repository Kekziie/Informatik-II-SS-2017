package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.RenderingHints;


import java.util.HashMap;

public class GraphicOutput implements AbstractGameFrontend, MouseListener {

  // thx Java for not having associative arrays
  HashMap<String, Color> colorPalette;

  private Rectangle gameArea;   // rectangle containing the game (position and shape)
  private PaintArea paintArea;  // panel containing the game (as an element)
  private JFrame gameFrame;     // entire window
  private int width, height, rows, cols, field_size, padding;
  private GameVersion game;
  private String title = "FourConnect";

  private int user_input;

  public void init(GameVersion game){
    int r = game.board.height;
    int c = game.board.width;
    this.game = game;
    user_input = -1;

    // colors from colorbrewer2.org
    colorPalette = new HashMap<String, Color>();
    colorPalette.put("player_a", new Color(43, 140, 190));
    colorPalette.put("player_b", new Color(49, 163, 84));
    colorPalette.put("lines", new Color(153, 153, 153));
    colorPalette.put("background", new Color(255, 255, 255));

    padding    = 6;                     // disk should be within the field
    field_size = 35;                    // width, height of each field
    rows       = r;                     // number of rows
    cols       = c;                     // number of columns
    width      = cols * field_size + 1; // estimate size of window
    height     = rows * field_size + 1; // estimate size of window

    // WNDCLASSEX-like stuff
    gameFrame = new JFrame(title);
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameFrame.setSize(width , height);
    gameFrame.setLocationRelativeTo(null); 
    gameFrame.setResizable(false);
    paintArea = new PaintArea();
    paintArea.setSize(width, height);
    gameFrame.add(paintArea);
    
    paintArea.setPreferredSize(new Dimension(width, height));
    gameFrame.pack();
    gameFrame.setVisible(true);
    gameFrame.validate();
    
    gameArea = new Rectangle(0, 0, width, height);
    paintArea.addMouseListener(this);
    
    
    
  }

  public GraphicOutput() {
    
  }
  

   // given coordinates, we want to indentify the corresponding field as a rectangle
  public Rectangle getFieldRectangle(int r, int c) {
    return new Rectangle(c * field_size - (int)field_size / 2, r * field_size - (int)field_size / 2, field_size, field_size);
  }

  // given a rectangle, we are interested in the center of the rectangle
  public Point fieldCenter(Rectangle rec) {
    return new Point((int)rec.getX() + (int)(rec.getWidth()) / 2, (int)rec.getY() + (int)(rec.getHeight()) / 2);
  }

  // draw a disk on a given field (r,c) where r=row and c=column
  public void drawDisk(Graphics2D g, int r, int c, Color color) {
    g.setColor(color);
    Point center = fieldCenter(getFieldRectangle(r, c));
    g.fillOval(center.x + padding / 2, center.y + padding / 2, field_size - padding, field_size - padding);
  }

  //
  public Point mouseToField(MouseEvent e) {
    int coordX = rows - (e.getY() / field_size ) - 1;
    int coordY = (e.getX() / field_size );
    return new Point(coordX, coordY);
  }
	
	public void drawBoard(GameVersion game){
		this.game.board = game.board;
    this.paintArea.repaint();
	}

   /*
  draws the board [rols][columns] where (0,0) is bottom left and (10,0) is above (0,0)
  */
  public void drawBoard(Graphics2D g, Rectangle area) {
    // TODO: use board via private member
    // for demo: we use hard-coded stuff first

    g.setColor(colorPalette.get("background"));
    g.fillRect((int)area.getX(), (int)area.getY(), (int)area.getWidth(), (int)area.getHeight());

    int step_hor = field_size;
    int step_ver = field_size;

    g.setColor(colorPalette.get("lines"));
    // draw vertical lines
    for (int c = 0; c <= cols; c++) {
      // x1, y1, x2, y2
      g.drawLine(c * step_hor, 0, c * step_hor, rows * step_ver);
    }
    // draw horizontal lines
    for (int r = 0; r <= rows; r++) {
      // x1, y1, x2, y2
      g.drawLine(0, r * step_ver, cols * step_hor,  r * step_ver);
    }

    for(int row = 0; row < this.game.board.height; row++){
      for(int col = 0; col < this.game.board.width; col++){
        if(this.game.board.getField(row, col) == 1){
          drawDisk(g, row, col, colorPalette.get("player_b"));
        }
        else if(this.game.board.getField(row, col) == 2){
          drawDisk(g, row, col, colorPalette.get("player_a"));
        }
      }
    }
  }

  private class PaintArea extends JPanel {
    public void paintComponent(Graphics g_) {
      // paint everything from parent
      super.paintComponent(g_);
      // we use another class, so let's cast "g_"
      Graphics2D g = (Graphics2D) g_;
      // the output of exercise assignments do not have to be ugly, let use increase the quality 
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      // draw the game board
      drawBoard(g, gameArea);
    }
  }

  public void mouseClicked(MouseEvent e) {
    Point clickedIn = mouseToField(e);
    user_input = (int)clickedIn.getY();
  }

  // we we need to have a plenty of these methods inherit from MouseListener
  public void mouseExited(MouseEvent e) {
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
  }

  public boolean waitForValidUserInput(GameVersion game ){
    return (user_input != -1);
  }

	public int getUserInput(GameVersion game ){
    int tmp = user_input;
    user_input = -1;
		return tmp;
	}
}
