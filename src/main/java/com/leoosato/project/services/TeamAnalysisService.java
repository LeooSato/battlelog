package com.leoosato.project.services;

import com.leoosato.project.analysis.TypeChart;
import com.leoosato.project.dto.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;
@ApplicationScoped
public class TeamAnalysisService {

    // pesos (mesmos da sua versão atual)
    private static final double DEF_W = 0.8;
    private static final double SPEED_BONUS = 0.15;
    private static final double IMMUNITY_BONUS = 0.40;
    private static final double FOURX_PENALTY = 0.80;

    @Inject PokemonService poke;

    public MatchupBreakdownResponse analyzeWithBreakdown(List<String> myIds, List<String> oppIds) {
        var my = myIds.stream().map(poke::getInfo).toList();
        var opp = oppIds.stream().map(poke::getInfo).toList();

        // countersByOpponent (top 3)
        Map<Integer, List<RankedMon>> counters = new HashMap<>();
        for (var o : opp) {
            var ranked = my.stream()
                    .map(m -> new RankedMon(m.id(), m.name(), round2(scorePairTotal(m, o))))
                    .sorted(Comparator.comparingDouble(RankedMon::score).reversed())
                    .limit(3)
                    .toList();
            counters.put(o.id(), ranked);
        }

        // leadOrder (média contra todo oponente)
        var lead = my.stream()
                .map(m -> new RankedMon(m.id(), m.name(),
                        round2(opp.stream().mapToDouble(o -> scorePairTotal(m, o)).average().orElse(0))))
                .sorted(Comparator.comparingDouble(RankedMon::score).reversed())
                .toList();

        // pairBreakdowns (componentes por par)
        List<PairBreakdown> pairBreakdowns = new ArrayList<>();
        for (var m : my) {
            for (var o : opp) {
                var b = scoreComponents(m, o);
                pairBreakdowns.add(new PairBreakdown(
                        m.id(), m.name(), o.id(), o.name(), b
                ));
            }
        }

        return new MatchupBreakdownResponse(lead, counters, pairBreakdowns);
    }

    // ---- cálculo dos componentes ----
    private ScoreBreakdown scoreComponents(PokemonService.PokemonInfo me, PokemonService.PokemonInfo o) {
        double offensive = TypeChart.bestStab(me.types(), o.types());
        double defensive = TypeChart.bestStab(o.types(), me.types());
        double speedBonus = Math.signum(me.speed() - o.speed()) * SPEED_BONUS;

        boolean immuneToSomeOppStab = hasStabImmunity(o.types(), me.types());
        boolean hasFourTimesWeakness = hasFourTimesWeakness(me.types(), o.types());

        double immunityBonus = immuneToSomeOppStab ? IMMUNITY_BONUS : 0.0;
        double fourXPenalty   = hasFourTimesWeakness ? FOURX_PENALTY : 0.0;

        double total = offensive - (defensive * DEF_W) + speedBonus + immunityBonus - fourXPenalty;

        return new ScoreBreakdown(
                round2(offensive),
                round2(defensive),
                round2(speedBonus),
                round2(immunityBonus),
                round2(fourXPenalty),
                round2(total)
        );
    }

    private double scorePairTotal(PokemonService.PokemonInfo me, PokemonService.PokemonInfo o) {
        return scoreComponents(me, o).total();
    }

    private boolean hasStabImmunity(List<String> oppStabTypes, List<String> myDefTypes) {
        for (String oppType : oppStabTypes) {
            if (TypeChart.mult(oppType, myDefTypes) == 0.0) return true;
        }
        return false;
    }

    private boolean hasFourTimesWeakness(List<String> myDefTypes, List<String> oppStabTypes) {
        for (String oppType : oppStabTypes) {
            if (TypeChart.mult(oppType, myDefTypes) >= 4.0) return true;
        }
        return false;
    }

    private static double round2(double v) {
        double r = Math.abs(v) < 1e-9 ? 0.0 : v;
        return Math.round(r * 100.0) / 100.0;
    }
}