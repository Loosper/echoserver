package org.elsys.netprog.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.map.ObjectMapper;

public class ShitcoinMiner {
	private static int RUNS = 10;
    private static String path = "http://localhost:8080/mining-pool/hash/";

	public static void main(String[] argv) {
		for (int i = 0; i < RUNS; i++) {
			InputContainer input = getHash();
			
			long now = Instant.now().getEpochSecond();
			System.out.println("got hash: " + input.HASH);
			System.out.println("got length: " + input.LENGTH);
			
			byte[] original = brute_force(HashClass.toBytes(input.HASH), input.LENGTH);
			System.out.println("found original: " + HashClass.toB64(original));
			long later = Instant.now().getEpochSecond();
			
			boolean valid = validate(new OutputContainer(original));
			System.out.println("Valid: " + Boolean.toString(valid));
			System.out.println("And it took: " + Long.toString(later - now) + " seconds\n");
		}
	}
	
	private static byte[] brute_force(byte[] hash, int length) {
//		Et tu, Brute?
		byte[] brute = new byte[length];
		Arrays.fill(brute, Byte.MIN_VALUE);
//		System.out.println(HashClass.toB64(brute));
		int i = brute.length - 1;
		
		while (true) {
//			for every value in a byte
			do {
//				System.out.println(HashClass.toB64(brute));
				if (Arrays.equals(hash, HashClass.hash(brute))) {
					return brute;
				}
				brute[i]++;
			} while(brute[i] != Byte.MIN_VALUE);
			
			for (int new_i = i - 1; i > 0; new_i--) {
				brute[new_i]++;
				if (brute[new_i] != Byte.MIN_VALUE) {
					break;
				}
			}
		}
	}

	private static InputContainer getHash() {
        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            System.out.println("GET request to: " + path);
            int resp = con.getResponseCode();
            System.out.println("Response code: " + Integer.toString(resp));

            if (resp != 200) {
            	throw new Exception("not 200");
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );

            String response = "";

            String line;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            in.close();

            ObjectMapper objectMapper = new ObjectMapper();
			InputContainer params = objectMapper.readValue(response, InputContainer.class);	
			
			return params;
        } catch (Exception e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
            System.exit(-1);
//            java wut?????
            return null;
        }
    }
	
	private static Boolean validate(OutputContainer out) {
        try {
        	ObjectMapper mapper = new ObjectMapper();
        	String json = mapper.writeValueAsString(out);
        	
			URL url = new URL(path);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("POST");
	        
	        con.setDoOutput(true);
	        con.setRequestProperty("Content-Type","application/json");  
	        OutputStream os = con.getOutputStream();
	        os.write(json.getBytes("UTF-8"));    
	        os.close();
	
	        System.out.println("POST request to: " + path);
	        int resp = con.getResponseCode();
	        System.out.println("Response code: " + Integer.toString(resp));
	
	        if (resp != 200) {
	        	return false;
	        } else {
	        	return true;
	        }
        } catch (Exception e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
            System.exit(-1);
//            java wut?????
            return false;
		}
	}
}
