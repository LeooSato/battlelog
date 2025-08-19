package com.leoosato.project.dto;

import java.util.List;

public class PokemonDTO {
    public String name;
    public List<String> types;

    public PokemonDTO() {}

    public PokemonDTO(String name, List<String> types) {
        this.name = name;
        this.types = types;
    }
}