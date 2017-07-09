package main;

public class GameBoard {
	protected int width;
	protected int height;
	// 0=leer, 1=Spieler A, 2=Spieler B
	protected int[][] fields;
	protected int[] fillState;
	protected boolean first_player;


	public GameBoard(int height, int width) {
		this.width = width;
		this.height = height;
		fields = new int[height][width];
		fillState = new int[width];
		first_player = true;
	}
	
	public GameBoard(GameBoard copy) {
		this.width = copy.width;
		this.height = copy.height;
		fields = new int[height][width];
		for(int i = 0; i < fields.length; i++) {
			System.arraycopy(copy.fields[i], 0, this.fields[i], 0, fields[i].length);
		}
		this.fillState = copy.fillState.clone();
		this.first_player = copy.first_player;
	}

	public int getField(int row, int col) {
		// ********************
		// Aufgabe a)
		return fields[row][col];
		// ********************
	}

	/*
	 * Set chip in field with column col with value player.
	 * In wich row the chip is set, depends on the fillstate of the column (filled from below).
	 *
	 * @param col, player
	 */
	public void playChip(int col, Player player) {
		// ********************
		// Aufgabe a)
		// Klasse muss selber verwalten, wer gerade am Zug war 
		int inputRow = fields.length - fillState[col] - 1;
		
		fields[inputRow][col] = first_player ? 1:2;
		
		first_player = !first_player;
		
		fillState[col]++;
		
		// ********************
	}

	public GameBoard deepCopy() {
		// ********************
		// Aufgabe a)
		GameBoard copy = new GameBoard(this);
		return copy;
		// ********************
	}

}
