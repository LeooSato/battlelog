package com.leoosato.project.dto;

public record ScoreBreakdown(
        double offensive,
        double defensive,
        double speedBonus,
        double immunityBonus,
        double fourXPenalty,
        double total
) {}