package main;

import java.util.concurrent.ThreadLocalRandom;

public class RandomPlayer implements Player {

	AbstractGameFrontend inputType;
	
	public RandomPlayer(AbstractGameFrontend inputType){
		this.inputType = inputType;
	}
	
	/*
	 * Plays a chip of the Player in the GameField, if the Input-Column is valid.
	 * While the userInput is not valid, write error-message and continue asking,
	 * until user entered a valid value.
	 * 
	 * @see main.Player#makeMove(main.GameVersion)
	 */
	public void makeMove(GameVersion game){
		
		int userInput;
		do {
			userInput = ThreadLocalRandom.current().nextInt(game.board.width);
		} while (!game.isValidMove(game.board, userInput));
		game.board.playChip(userInput, this);
	}
}
