package com.leoosato.project.analysis;

import java.util.*;

public class TypeChart {
    // nomes conforme PokeAPI
    public static final List<String> TYPES = List.of(
            "normal","fire","water","electric","grass","ice","fighting","poison",
            "ground","flying","psychic","bug","rock","ghost","dragon","dark","steel","fairy"
    );
    private static final Map<String, Integer> IDX = new HashMap<>();
    static { for (int i=0;i<TYPES.size();i++) IDX.put(TYPES.get(i), i); }

    // matriz atacante x defensor (ex.: FIRE vs GRASS = 2.0)
    private static final double[][] M = new double[18][18];
    static {
        // inicializa com neutro
        for (int i=0;i<18;i++) for (int j=0;j<18;j++) M[i][j] = 1.0;

        // a seguir setamos apenas onde muda (resumo correto do type chart clássico):
        set("normal","rock",0.5); set("normal","ghost",0.0); set("normal","steel",0.5);

        set("fire","fire",0.5); set("fire","water",0.5); set("fire","grass",2); set("fire","ice",2);
        set("fire","bug",2); set("fire","rock",0.5); set("fire","dragon",0.5); set("fire","steel",2);

        set("water","fire",2); set("water","water",0.5); set("water","grass",0.5);
        set("water","ground",2); set("water","rock",2); set("water","dragon",0.5);

        set("electric","water",2); set("electric","electric",0.5); set("electric","grass",0.5);
        set("electric","ground",0.0); set("electric","flying",2); set("electric","dragon",0.5);

        set("grass","fire",0.5); set("grass","water",2); set("grass","grass",0.5);
        set("grass","poison",0.5); set("grass","ground",2); set("grass","flying",0.5);
        set("grass","bug",0.5); set("grass","rock",2); set("grass","dragon",0.5); set("grass","steel",0.5);

        set("ice","fire",0.5); set("ice","water",0.5); set("ice","grass",2); set("ice","ice",0.5);
        set("ice","ground",2); set("ice","flying",2); set("ice","dragon",2); set("ice","steel",0.5);

        set("fighting","normal",2); set("fighting","ice",2); set("fighting","rock",2); set("fighting","dark",2); set("fighting","steel",2);
        set("fighting","poison",0.5); set("fighting","flying",0.5); set("fighting","psychic",0.5); set("fighting","bug",0.5); set("fighting","fairy",0.5);
        set("fighting","ghost",0.0);

        set("poison","grass",2); set("poison","poison",0.5); set("poison","ground",0.5); set("poison","rock",0.5);
        set("poison","ghost",0.5); set("poison","steel",0.0); set("poison","fairy",2);

        set("ground","fire",2); set("ground","electric",2); set("ground","poison",2); set("ground","rock",2); set("ground","steel",2);
        set("ground","grass",0.5); set("ground","bug",0.5); set("ground","flying",0.0);

        set("flying","grass",2); set("flying","fighting",2); set("flying","bug",2);
        set("flying","electric",0.5); set("flying","rock",0.5); set("flying","steel",0.5);

        set("psychic","fighting",2); set("psychic","poison",2);
        set("psychic","psychic",0.5); set("psychic","steel",0.5); set("psychic","dark",0.0);

        set("bug","grass",2); set("bug","psychic",2); set("bug","dark",2);
        set("bug","fire",0.5); set("bug","fighting",0.5); set("bug","poison",0.5);
        set("bug","flying",0.5); set("bug","ghost",0.5); set("bug","steel",0.5); set("bug","fairy",0.5);

        set("rock","fire",2); set("rock","ice",2); set("rock","flying",2); set("rock","bug",2);
        set("rock","fighting",0.5); set("rock","ground",0.5); set("rock","steel",0.5);

        set("ghost","psychic",2); set("ghost","ghost",2); set("ghost","dark",0.5); set("ghost","normal",0.0);

        set("dragon","dragon",2); set("dragon","steel",0.5); set("dragon","fairy",0.0);

        set("dark","psychic",2); set("dark","ghost",2); set("dark","fighting",0.5); set("dark","dark",0.5); set("dark","fairy",0.5);

        set("steel","ice",2); set("steel","rock",2); set("steel","fairy",2);
        set("steel","fire",0.5); set("steel","water",0.5); set("steel","electric",0.5); set("steel","steel",0.5);

        set("fairy","fighting",2); set("fairy","dragon",2); set("fairy","dark",2);
        set("fairy","fire",0.5); set("fairy","poison",0.5); set("fairy","steel",0.5);
    }

    private static void set(String atk, String def, double v) {
        M[IDX.get(atk)][IDX.get(def)] = v;
    }

    // multiplicador de dano de um tipo atacante contra 1 ou 2 tipos defensores
    public static double mult(String atkType, List<String> defTypes) {
        int ai = IDX.getOrDefault(atkType, -1);
        if (ai < 0) return 1.0;
        double x = 1.0;
        for (String def : defTypes) {
            Integer di = IDX.get(def);
            x *= (di == null ? 1.0 : M[ai][di]);
        }
        return x;
    }

    // melhor multiplicador ofensivo considerando "STAB" dos 1–2 tipos do atacante
    public static double bestStab(List<String> myTypes, List<String> defTypes) {
        double best = 1.0;
        for (String t : myTypes) {
            best = Math.max(best, mult(t, defTypes));
        }
        return best;
    }
}