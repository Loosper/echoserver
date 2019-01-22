package org.elsys.netprog.rest;

public class HashContainer {
	public byte[] HASH;
	public int LENGTH;
	
	private byte[] input;
	
	public HashContainer(byte[] bytes) {
		this.LENGTH = bytes.length;
		this.HASH = HashClass.hash(bytes);
		this.input = bytes;
	}
}
