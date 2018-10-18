package org.elsys.netprog.rest;

import org.elsys.netprog.rest.Game;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	@POST
	@Path("/startGame")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response startGame() throws URISyntaxException {
		return Response.created(new URI("/games")).entity(Game.newGame()).build();
	}
	
	@PUT
	@Path("/guess/{id}/{guess}")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response guess(@PathParam("id") String gameId, @PathParam("guess") String guess) throws Exception{
		if (!Game.exists(gameId)) {
			return Response.status(404).build();
		}
		
		String result = Game.makeGuess(gameId, guess);
		if (result.equals(""))
			return Response.status(400).build();
		
		return Response.status(200).entity(result).build();
		
	}
	
	@GET
	@Path("/games")
	@Produces(value={MediaType.APPLICATION_JSON})
//	you know, if this wasn't here, i could implement encrypted tokens that are held by the users,
//	which would contain ALL the information they need to play the game. No need for database (static arrays)
	public Response getGames() {
		return Response.status(404).entity(Game.allGames()).build();
	}
}
