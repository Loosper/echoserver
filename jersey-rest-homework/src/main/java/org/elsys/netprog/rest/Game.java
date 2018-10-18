package org.elsys.netprog.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Random;
import java.util.UUID;

public class Game {
	private static HashMap<String, Boolean> success_store = new HashMap<String, Boolean>(); 
	private static HashMap<String, String> secret_store = new HashMap<String, String>();
	private static HashMap<String, Integer> turn_store = new HashMap<String, Integer>();
	private static Random randomiser = new Random();

	public static String newGame() {
		String identifier = UUID.randomUUID().toString();
		String cows = "";
		
		while (cows.length() < 4) {
			char val = (char) (randomiser.nextInt(9) + 48);
			boolean repeated = false;
			
			for (char letter: cows.toCharArray()) {
				if (letter == val) {
					repeated = true;
					break;
				}
			}
			
			if (!repeated) {
				cows = cows + val;
			}
		}
		
		turn_store.put(identifier, 0);
		secret_store.put(identifier, cows);
		return identifier;
	}
	
	public static boolean exists(String id) {
		return secret_store.containsKey(id);
	}
	
	public static boolean validGuess(String guess) {
		int count = (int) guess.chars().distinct().count();
		
		if (count != 4)
			return false;
		
		return true;
	}
	
	private static ArrayList<Character> StringToList(String str) {
		ArrayList<Character> list = new ArrayList<Character>();
		
		for (char ch: str.toCharArray()) {
			list.add(ch);
		}
		
		return list;
	}
	
	public static String makeGuess(String id, String guess) {
		if (!validGuess(guess))
			return "";
		
		int turns = turn_store.get(id);
		int bulls = 0;
		int cows = 0;
		turns += 1;
		turn_store.put(id, turns);
		
		ArrayList<Character> secret = StringToList(secret_store.get(id));
		ArrayList<Character> attempt = StringToList(guess);;
		
		for (int i = 0; i < 4; i++) {
			if (secret.get(i) == attempt.get(i)) {
				secret.remove(i);
				attempt.remove(i);
				bulls += 1;
			}
		}
		
//		moo
		for (char ch: secret) {
			if (attempt.contains(secret))
				cows += 1;
		}
		
		boolean success = cows == bulls;
		success_store.put(id, success);
		
//		you know what, fuck this shit. It's a shame this is more sane
		String response = "{" + 
			"\"gameId\":" + id + "," + 
			"\"cowsNumber\":" + Integer.toString(cows) + "," +
			"\"bullsNumber\":" + Integer.toString(bulls) + "," +
			"\"success\":" + success + 
		"}";
		
		return response;
	}
	
	public static String allGames() {
		String response = "[";
		ArrayList<String> elements = new ArrayList<String>();
		Set<String> ids = secret_store.keySet();
		
		for (String id: ids) {
			elements.add("{" + 
				"\"gameId\":"+ id + "," +
				"\"turnsCount\":"+ Integer.toString(turn_store.get(id)) + "," +
				"\"secret\":"+ secret_store.get(id) + "," +
				"\"success\":"+ Boolean.toString(success_store.get(id)) +
			"}");
		}
		
		for (int i = 0; i < elements.size(); i++) {
			response = response + elements.get(i);
			if (i != elements.size() - 1) {
				response = response + ",";
			}
		}
		
		return response;
	}
}
