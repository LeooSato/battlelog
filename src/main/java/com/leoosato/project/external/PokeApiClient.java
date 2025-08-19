package com.leoosato.project.external;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.JsonNode;

@RegisterRestClient(configKey = "pokeapi")
@Path("/pokemon")
@Produces(MediaType.APPLICATION_JSON)
public interface PokeApiClient {
    @GET @Path("/{idOrName}")
    JsonNode getPokemon(@PathParam("idOrName") String idOrName);
}