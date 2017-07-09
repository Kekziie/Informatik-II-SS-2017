package main;

import java.util.Scanner;

public class ConsoleOutput implements AbstractGameFrontend {
	
	private int user_input;

	
	public void init(GameVersion game) {
		
		user_input = -1;
		
	}

	
	public void drawBoard(GameVersion board) {
		
		StringBuilder stringBoard = new StringBuilder();
		for(int i = 0; i < board.board.height; i++) {
			for (int j = 0; j < board.board.width; j++) {
				
				switch (board.board.fields[i][j]) {
				case 0:
				    stringBoard.append(". ");
				    break;
				case 1:
					stringBoard.append("x ");
					break;
				case 2:
					stringBoard.append("o ");
					break;
				}		
			}
			
			stringBoard.append("\n");
		}
		
		System.out.print(stringBoard.toString());
	}

	
	public int getUserInput(GameVersion game) {
		
		int data = user_input;
		user_input = -1;
		return data;
	}

	
	public boolean waitForValidUserInput(GameVersion game) {
		
		Scanner sc = new Scanner(System.in);
		user_input = sc.nextInt();
		boolean validUserInput = game.isValidMove(game.board, user_input);
		return validUserInput;
	}
}
