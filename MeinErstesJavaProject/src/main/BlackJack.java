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
		int currentBalance;
		int cardValuePlayer;
		int cardValueBank;

		//Hilfestellung für den Scanner:	
		System.out.print("Geben Sie einen Wert ein, welcher in der Variable 'testVariable' gespeichert werden soll:");
		//Scanner initialisieren
		Scanner s = new Scanner(System.in);		
		//So wird die Eingabe in die Variable "testVariable" gespeichert
		int testVariable = s.nextInt();
	}


	private static String evaluateWinner(int cardValuePlayer, int cardValueBank) {

		return "String"; //TODO: "String" durch den Sieger ersetzen (siehe Übungsblatt)
	}

	private static int updateMoney(int currentBalance, int bet, String winner) {

		return 0; //TODO: Hier den Wert des aktualisierten Kontostandes zurückgeben
	}


}



