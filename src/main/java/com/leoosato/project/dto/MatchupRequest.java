package com.leoosato.project.dto;
import java.util.List;

public record MatchupRequest(
        List<String> myTeam,   // ids ou nomes da PokeAPI (ex: ["6","vaporeon","282"])
        List<String> opponent  // ex: ["dragonite","248","empoleon","91"]
) {}