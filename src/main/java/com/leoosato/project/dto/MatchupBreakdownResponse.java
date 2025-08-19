package com.leoosato.project.dto;

import java.util.List;
import java.util.Map;

public record MatchupBreakdownResponse(
        List<RankedMon> leadOrder,
        Map<Integer, List<RankedMon>> countersByOpponent,
        List<PairBreakdown> pairBreakdowns
) {}