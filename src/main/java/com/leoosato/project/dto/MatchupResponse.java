package com.leoosato.project.dto;

import java.util.List;
import java.util.Map;

public record MatchupResponse(
        List<RankedMon> leadOrder,
        Map<Integer, List<RankedMon>> countersByOpponent,   // oponenteId -> top respostas
        Map<Integer, List<Integer>> favorableTargetsByMyMon // meuId -> lista de oponentes favoráveis
) {}