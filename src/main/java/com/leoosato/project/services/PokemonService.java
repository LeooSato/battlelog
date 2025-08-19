package com.leoosato.project.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.leoosato.project.dto.PokemonDTO;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.leoosato.project.external.PokeApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class PokemonService {

    @Inject @RestClient PokeApiClient pokeApi;

    public static record PokemonInfo(int id, String name, List<String> types, int speed) {}

    @CacheResult(cacheName = "pokeapi-pokemon")
    public JsonNode getRaw(String idOrName) {
        return pokeApi.getPokemon(idOrName.toLowerCase());
    }

    public PokemonDTO getPokemon(String idOrName) {
        JsonNode json = getRaw(idOrName);

        String name = json.get("name").asText();
        List<String> types = StreamSupport.stream(json.get("types").spliterator(), false)
                .map(t -> t.get("type").get("name").asText())
                .collect(Collectors.toList());

        return new PokemonDTO(name, types);
    }

    public PokemonInfo getInfo(String idOrName) {
        JsonNode n = getRaw(idOrName);
        int id = n.get("id").asInt();
        String name = n.get("name").asText();

        List<String> types = new ArrayList<>();
        n.get("types").forEach(t -> types.add(t.get("type").get("name").asText())); // ex: ["fire","flying"]

        int speed = 0;
        for (var s : n.get("stats")) {
            if (s.get("stat").get("name").asText().equals("speed")) {
                speed = s.get("base_stat").asInt();
            }
        }
        return new PokemonInfo(id, name, types, speed);
    }

}
