package org.elsys.netprog.rest;

public class GuessContainer {
	public String gameId;
	public int cowsNumber;
	public int bullsNumber;
	public int turnsCount;
	public boolean success;
	
	public GuessContainer(String id, int cows, int bulls, int turns, boolean success) {
		gameId = id;
		cowsNumber = cows;
		bullsNumber = bulls;
		turnsCount = turns;
		this.success = success;
	}
}
