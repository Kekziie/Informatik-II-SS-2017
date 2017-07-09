package main;

import java.io.ObjectInputStream.GetField;

public class GameVersion {
	protected GameBoard board;

	public GameVersion(GameBoard board) {
		this.board = board;
	}

	public void nextRound() {}

	/*
	 * Writes a '1' in the return array for all valid columns (columns which are not full),
	 * and a '0' for all invalid columns (full columns);
	 *
	 * @return int[]
	 */
	public int[] getValidColumns() {
		// ********************
		// Aufgabe b)
		// 0=not valid, 1=valid
		int[] validColumns = new int[board.width];
		for(int i = 0; i < validColumns.length ; i++) {
			
			validColumns[i] = !isValidMove(board, i) ? 0:1;
		}
		
		return validColumns;
		// ********************
	}

	/*
	 * Returns if it is valid to put a chip in the column 'col' of the gameboard 'gameBoard'
	 *
	 * @param GameBoard, int
	 * @return boolean
	 */
	public boolean isValidMove(GameBoard gameBoard, int col) {
		// ********************
		// Aufgabe b)
		return gameBoard.fillState[col] == gameBoard.height ? false:true;
		// ********************
	}


	private int wonHorizontal() {
		// ********************
		// Aufgabe b)
		for (int row = 1; row < board.height; row++) {
			int balance = 1;
			int current = board.getField(row, 0);
			for (int col = 0; col < board.width; col++) {
				if (current == board.getField(row, col )) {
					// one consecutive stone
					balance++;
				} else {
					// nope, other player restart counting
					balance = 1;
					current = board.getField(row, col );
				}
				// 4 same fields, but not empty
				if ((balance == 4) && (current != 0)) {
					return current;
				}
			}
		}
		return 0;
		// ********************
	}

	private int wonVertical() {
		// search for 4 consecutive "1"s or "2"s
		// count for balance of consecutive chips
		for (int col = 0; col < board.width; col++) {
			int balance = 1;
			int current = board.getField(0, col);
			for (int row = 1; row < board.height; row++) {
				if (current == board.getField(row, col )) {
					// one consecutive stone
					balance++;
				} else {
					// nope, other player restart counting
					balance = 1;
					current = board.getField(row, col );
				}
				// 4 same fields, but not empty
				if ((balance == 4) && (current != 0)) {
					return current;
				}
			}
		}
		return 0;
	}

	private int wonDiagonals() {
		for (int row = 3; row < board.height ; row++) {
			for (int col = 0; col < board.width-3 ; col++) {
				// this is the field, where we start counting
				int consecutive = 1;
				int test_for = board.getField(row, col);
				if (test_for != 0) {
					for (int offset = 1; offset < 4; offset++) {
						if (board.getField(row - offset, col + offset) == test_for) {
							consecutive++;
							if (consecutive == 4)
								return test_for;
						} else {
							break;
						}
					}
				}

			}
		}
		for (int row = 0; row < board.height -3; row++) {
			for (int col = 0; col < board.width-3 ; col++) {
				// this is the field, where we start counting
				int consecutive = 1;
				int test_for = board.getField(row, col);
				if (test_for != 0) {
					for (int offset = 1; offset < 4; offset++) {
						if (board.getField(row + offset, col + offset) == test_for) {
							consecutive++;
							if (consecutive == 4)
								return test_for;
						} else {
							break;
						}
					}
				}

			}
		}

		return 0;
	}


	public String playerToString(int i) {
		if (i == 1) {
			return "Spieler 1";
		}
		if (i == 2) {
			return "Spieler 2";
		}
		return "none";
	}

	public String finished() {

		int rsl_horizontal = wonHorizontal();
		if (rsl_horizontal != 0) {
			return playerToString(rsl_horizontal);
		}

		int rsl_vertical = wonVertical();
		if (rsl_vertical != 0) {
			return playerToString(rsl_vertical);
		}

		int rsl_diagonal = wonDiagonals();
		if (rsl_diagonal != 0) {
			return playerToString(rsl_diagonal);
		}

		return "none";

	}


}