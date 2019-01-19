package org.elsys.netprog.rest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64;

public class HashClass {
	public static byte[] hash(byte[] input) {
		MessageDigest hasher;
		try {
			hasher = MessageDigest.getInstance("MD5");			
		} catch (NoSuchAlgorithmException e) {
			// don't care, will never happen
			return new byte[1];
		}
		
		return hasher.digest(input);
	}
	
	public static String toB64(byte[] input) {
		return new String(Base64.getEncoder().encode(input));
	}
	
	public static byte[] toBytes(String input) {
		try {
			return Base64.getDecoder().decode(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
//			will never happen
			return new byte[1];
		} catch (IllegalArgumentException e) {
			return new byte[1];
		}
	}
}
