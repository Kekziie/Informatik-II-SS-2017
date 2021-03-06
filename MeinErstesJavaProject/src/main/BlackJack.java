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
			// setzt die Werte auf Null
			cardValuePlayer = 0;
			cardValueBank = 0;
			do {
				// zeigt aktuellen Wert und fragt nach dem Einsatz
				System.out.print("Your amount is " + currentBalance + "$. Please place your bet: ");
				// gespeicherter Input
				bet = s.nextInt();
				// c) abfangen der Fälle bei ungültigen Beträgen
				if (bet <= 0 || bet > currentBalance)
					System.out.println("Illegal bet");
			} while (bet <= 0 || bet > currentBalance);
			currentBalance -=bet;
			// Schleife für die Kartenziehphase
			do {
				cardValuePlayer += giveCard();
				System.out.println("Your cardvalue is: " + cardValuePlayer);
			} while (cardValuePlayer <21 && oneMoreCard());
			if (cardValuePlayer < 21) {
				do {
					// zieht Karte
					cardValueBank += giveCard();
					// gibt neuen Wert an
					System.out.println("The bank's cardvalue is :" + cardValueBank);
				} while(cardValueBank < cardValuePlayer && cardValueBank < 17);
			}
			// gibt Gewinner an
			String winner = evaluateWinner(cardValuePlayer, cardValueBank);
			switch(winner) {
			case "player":
				System.out.println("You won!" + "(" + cardValuePlayer + " / " + cardValueBank + ")");
				break;
			case "bank":
			    System.out.println("You lost!" + "(" + cardValuePlayer + " / " + cardValueBank + ")");
			    break;
			case "both":
		    System.out.println("Tied!" + "(" + cardValuePlayer + " / " + cardValueBank + ")");
		    break;    
			}
			currentBalance = updateMoney(currentBalance, bet, winner);
			// fragt nach der nächsten Runde
			System.out.println("Are you ready for the next round?");
			if (Character.toLowerCase(s.next().charAt(0)) != 'y') {
				System.out.println("Your final balance was: " + currentBalance + "$");
				System.exit(0);
			}
			// Spieler hat kein Geld mehr übrig
			if (currentBalance == 0) {
				System.out.println("You have no cash anymore");
				System.exit(0);
			// Spieler hat Schulden	
			} else if (currentBalance < 0) {
				System.out.println("Gambling is addictive. You should have stopped when you won. You are now in debt!");
				System.exit(0);
			}
		
		}

	}
	
	// d) zufällige Karte zwischen 1 und 11
	public static int giveCard() {
		return (int)(Math.random() * 11 + 1);
	}
	
	// e) Abfrage, ob eine weiter Karte gezogen werden soll
	public static boolean oneMoreCard() {
		Scanner s = new Scanner(System.in);
		System.out.println("Card?");
		return (Character.toLowerCase(s.next().charAt(0)) == 'y');
	}

	// f) gibt Gewinner als String zurück
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
	
    // g) gibt Wert des aktuellen Kontostandes zurück
	public static int updateMoney(int currentBalance, int bet, String winner) {
		
		switch(winner) {
		case "player":
			return currentBalance + 2 * bet;
		case "bank":
			return currentBalance;
		default:
			return currentBalance + bet; 
			
		}
	}

}






