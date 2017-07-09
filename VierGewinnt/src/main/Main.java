package main;
import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {

		int height = 7;
		int width = 8;
		GameBoard game_board = new GameBoard(height, width);
		AbstractGameFrontend frontend = new ConsoleOutput();
		GameVersion game_version = new FourConnect(game_board);

		frontend.init(game_version);
		frontend.drawBoard(game_version);

		Player player_a = new HumanPlayer(frontend);
		Player player_b = new RandomPlayer(frontend);

		String winner = game_version.finished();


		while (true) {
			game_version.nextRound();

			player_a.makeMove(game_version);
			frontend.drawBoard(game_version);
			winner = game_version.finished();
			if(!winner.equals("none"))
				break;

			player_b.makeMove(game_version);
			frontend.drawBoard(game_version);
			winner = game_version.finished();
			if(!winner.equals("none"))
				break;

		}

		System.out.println("Gewinner: " + game_version.finished());


	}

}
