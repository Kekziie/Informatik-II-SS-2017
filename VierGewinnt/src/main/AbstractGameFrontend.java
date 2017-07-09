package main;

public interface AbstractGameFrontend {

	public void init(GameVersion game);
	public void drawBoard(GameVersion board);
	public int getUserInput(GameVersion game);
	public boolean waitForValidUserInput(GameVersion game);
	
}
