package com.leoosato.project.resource;

import com.leoosato.project.dto.PokemonDTO;
import com.leoosato.project.services.PokemonService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/pokemon")
@Produces(MediaType.APPLICATION_JSON)
public class PokeResource {

    @Inject PokemonService pokemonService;

    @GET
    @Path("/{idOrName}")
    public PokemonDTO get(@PathParam("idOrName") String idOrName) {
        return pokemonService.getPokemon(idOrName);
    }
}
