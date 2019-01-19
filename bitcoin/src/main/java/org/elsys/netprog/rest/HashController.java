package org.elsys.netprog.rest;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


@Path("/hash")
public class HashController {
	private static Random randomiser = new Random();
	private static int MAXBYTES = 10;
	private static List<String> inputs = new ArrayList();
	
	private static byte[] randomBytes(int nBytes) {
		byte[] seq = new byte[nBytes];
		Random randomiser = new Random();
		
		randomiser.nextBytes(seq);
		
		return seq;
	}
	
	@GET
	@Path("/")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response start() throws URISyntaxException {
		
		// [0; max) -> [1; max]
		int nBytes = this.randomiser.nextInt(this.MAXBYTES) + 1;
		byte[] input = this.randomBytes(nBytes);
		String encoded = HashClass.toB64(input);
		this.inputs.add(encoded);
		
		System.out.println("Generated: " + encoded);
		
		return Response.ok().entity(new HashContainer(input)).build();
	}
	
	private static Response error() {
		System.out.println("Wrong guess");
		return Response.status(406).build();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response guess(String inputJson) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			InputContainer params = objectMapper.readValue(inputJson, InputContainer.class);			
		
			System.out.println("Got: input='" + params.INPUT + "' hash='" + params.HASH + "'");
			byte[] input = HashClass.toBytes(params.INPUT);
			byte[] hash = HashClass.toBytes(params.HASH);
			
			if (!this.inputs.contains(params.INPUT)) { 
				return this.error();
			}
			
			if (!Arrays.equals(HashClass.hash(input), hash)) {
				return this.error();
			}
			
			System.out.println("Guess correct");
			
			return Response.ok().build();
		} catch (Exception e) {
			return this.error();
		}
	}
}
