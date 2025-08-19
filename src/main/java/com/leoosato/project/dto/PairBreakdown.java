package com.leoosato.project.dto;

public record PairBreakdown(
        int myPokemonId,
        String myPokemonName,
        int oppPokemonId,
        String oppPokemonName,
        ScoreBreakdown breakdown
) {}