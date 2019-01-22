package org.elsys.netprog.rest;

public class OutputContainer {
	public String INPUT;
	public String HASH;
	
	public OutputContainer(byte[] input) {
		INPUT = HashClass.toB64(input);
		HASH = HashClass.toB64(HashClass.hash(input));
	}
}
