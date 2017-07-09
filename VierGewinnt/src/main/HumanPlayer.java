package main;
import java.lang.Thread;

public class HumanPlayer implements Player {

	AbstractGameFrontend inputType;
	
	public HumanPlayer(AbstractGameFrontend inputType){
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
		
		while(!this.inputType.waitForValidUserInput(game)){
			try {Thread.sleep(500); }catch(InterruptedException e) {}
		}
		int userInput = this.inputType.getUserInput(game);
		game.board.playChip(userInput, this);
	}
}
