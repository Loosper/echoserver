package org.elsys.netprog.rest;

public class ListContainer {
	public String gameId;
	public int turnsCount;
	public String secret;
	public boolean success;
	
	public ListContainer(String id, int turns, String secret, boolean success) {
		gameId = id;
		turnsCount = turns;
		this.secret = secret;
		this.success = success;
	}
}
