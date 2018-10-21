package org.elsys.netprog.rest;

import org.elsys.netprog.rest.Game;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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


@Path("/game")
public class GameController {
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
		success_store.put(identifier, false);
		return identifier;
	}
	
	public static boolean exists(String id) {
		return secret_store.containsKey(id);
	}
	
	public static boolean validGuess(String guess) {
		int count = (int) guess.chars().distinct().count();
		
		if (count != 4)
			return false;
		
		if (!guess.matches("[123456789]\\d{3}"))
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
	
	
	
	@POST
	@Path("/startGame")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response startGame() throws URISyntaxException {
		return Response.created(new URI("/games")).entity(newGame()).build();
	}
	
	@PUT
	@Path("/guess/{id}/{guess}")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response guess(@PathParam("id") String id, @PathParam("guess") String guess) throws Exception{
		if (!exists(id)) {
			return Response.status(404).build();
		}
		
		if (!validGuess(guess))
			return Response.status(400).build();

		int turns = turn_store.get(id);
		int bulls = 0;
		int cows = 0;
		turns += 1;
		turn_store.put(id, turns);
		
		ArrayList<Character> secret = StringToList(secret_store.get(id));
		ArrayList<Character> attempt = StringToList(guess);
		
		for (int i = 0; i < 4; i++) {
			if (secret.get(i) == attempt.get(i)) {
				bulls += 1;
			} else if (attempt.contains(secret.get(i))) {
//				moo
				cows += 1;
			}
		}
		
		boolean success = bulls == 4;
		success_store.put(id, success);
		
		return Response.status(200).entity(new GuessContainer(id, cows, bulls, turns, success)).build();
		
	}
	
	@GET
	@Path("/games")
	@Produces(value={MediaType.APPLICATION_JSON})
//	put the state into 'stateless'
	public Response getGames() {
		ArrayList<ListContainer> elements = new ArrayList<ListContainer>();
		Set<String> ids = secret_store.keySet();
		
		for (String id: ids) {
			boolean success = success_store.get(id);
			elements.add(new ListContainer(id, turn_store.get(id), (success) ? secret_store.get(id) : "****", success));
		}
		
		return Response.ok().entity(elements).build();
	}
}
