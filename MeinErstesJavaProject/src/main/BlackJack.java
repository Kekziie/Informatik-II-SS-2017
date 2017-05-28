package main;

import java.util.Scanner;

public class BlackJack {

	public static void main(String[] args){
		//Das Spiel soll mit dem Aufruf der Funktion startGame(int startMoney) gestartet werden.
		startGame(1000);
	}

	public static void startGame(int startMoney){
		System.out.println("Welcome to BlackJack");

		//TODO: in der Variable currentBalance soll der aktuelle Kontostand des Spielers gespeichert werden
		//TODO: in der Variable cardValuePlayer soll der Kartenwert des Spielers gespeichert werden
		//TODO: in der Variable cardValieBank soll der Kartenwert der Bank gespeichert werden
		int currentBalance = startMoney, cardValuePlayer,cardValueBank, bet=0;		

		while(true) {
			Scanner s = new Scanner(System.in);
			cardValuePlayer = 0;
			cardValueBank = 0;
			do {
				System.out.print("Your amount is " + currentBalance + "$. Please place your bet: ");
				bet = s.nextInt();
				if (bet <= 0 || bet > currentBalance)
					System.out.println("Illegal bet");
			} while (bet <= 0 || bet > currentBalance);
			currentBalance -=bet;
			do {
				cardValuePlayer += giveCard();
				System.out.println("Your cardvalue is: " + cardValuePlayer);
			} while (cardValuePlayer <21 && oneMoreCard());
			if (cardValuePlayer < 21) {
				do {
					cardValueBank += giveCard();
					System.out.println("The bank's cardvalue is :" + cardValueBank);
				} while(cardValueBank < cardValuePlayer && cardValueBank < 17);
			}
			String winner = evaluateWinner(cardValuePlayer, cardValueBank);
			switch(winner) {
			case "player":
				System.out.println("You won!" + "(" + cardValuePlayer + " / " + cardValueBank);
				break;
			case "bank":
			    System.out.println("You lost!" + "(" + cardValuePlayer + " / " + cardValueBank);
			    break;
			case "both":
		    System.out.println("Tied!" + "(" + cardValuePlayer + " / " + cardValueBank);
		    break;    
			}
			currentBalance = updateMoney(currentBalance, bet, winner);
			System.out.println("Are you ready for the next round?");
			if (Character.toLowerCase(s.next().charAt(0)) != 'y') {
				System.out.println("Your final balance was: " + currentBalance + "$");
				System.exit(0);
			}
			if (currentBalance == 0) {
				System.out.println("You have no cash anymore");
				System.exit(0);
			} else if (currentBalance < 0) {
				System.out.println("Gambling is addictive. You should have stopped when you won. You are now in debt!");
				System.exit(0);
			}
		
		}

	}
	
	public static int giveCard() {
		return (int)(Math.random() * 11 + 1);
	}
	
	public static boolean oneMoreCard() {
		Scanner s = new Scanner(System.in);
		System.out.println("Card?");
		return (Character.toLowerCase(s.next().charAt(0)) == 'y');
	}

	public static String evaluateWinner(int cardValuePlayer, int cardValueBank) {

		if (cardValuePlayer > 21)
			return "bank";
		else if (cardValueBank > 21)
			return "player";
		else if (Math.abs(21-cardValuePlayer)>(Math.abs(21-cardValueBank)))
			return "bank";
		else if (Math.abs(21-cardValuePlayer)<(Math.abs(21-cardValueBank)))
			return "player";
		else return "both";
	}

	public static int updateMoney(int currentBalance, int bet, String winner) {
		
		switch(winner) {
		case "player":
			return currentBalance + 2 * bet;
		case "bank":
			return currentBalance;
		case "both":
			return currentBalance + bet; 
			
		}
	}

}






