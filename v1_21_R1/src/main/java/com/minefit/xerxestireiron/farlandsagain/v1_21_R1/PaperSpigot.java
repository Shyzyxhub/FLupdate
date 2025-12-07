package com.minefit.xerxestireiron.farlandsagain.v1_21_R1;

import org.spigotmc.SpigotWorldConfig;

import java.lang.reflect.Field;

/**
 * Wrapper léger pour détecter les options spécifiques Paper d'un monde.
 * Compatible Paper 1.21.4.
 */
public class PaperSpigot {

    public final boolean generateCanyon;
    public final boolean generateCaves;
    public final boolean generateDungeon;
    public final boolean generateFortress;
    public final boolean generateMineshaft;
    public final boolean generateMonument;
    public final boolean generateStronghold;
    public final boolean generateTemple;
    public final boolean generateVillage;
    public final boolean generateFlatBedrock;

    public PaperSpigot(String worldName, boolean isPaper) {
        boolean flatBedrock = false;

        if (isPaper) {
            try {
                // PaperWorldConfig peut exister : on utilise la reflection
                Class<?> pwcClass = Class.forName("com.destroystokyo.paper.PaperWorldConfig");
                Object paperConfig = pwcClass.getConstructor(String.class, SpigotWorldConfig.class)
                        .newInstance(worldName, new SpigotWorldConfig(worldName));

                try {
                    Field f = pwcClass.getDeclaredField("generateFlatBedrock");
                    f.setAccessible(true);
                    Object val = f.get(paperConfig);
                    if (val instanceof Boolean) flatBedrock = (Boolean) val;
                } catch (NoSuchFieldException ignored) {
                    // fallback si le nom du champ a changé
                }
            } catch (Throwable ignored) {
                // PaperWorldConfig absent ou échec de reflection : on utilise les valeurs par défaut
            }

            this.generateCanyon = true;
            this.generateCaves = true;
            this.generateDungeon = true;
            this.generateFortress = true;
            this.generateMineshaft = true;
            this.generateMonument = true;
            this.generateStronghold = true;
            this.generateTemple = true;
            this.generateVillage = true;
            this.generateFlatBedrock = flatBedrock;

        } else {
            // Valeurs par défaut pour Spigot / Bukkit
            this.generateCanyon = true;
            this.generateCaves = true;
            this.generateDungeon = true;
            this.generateFortress = true;
            this.generateMineshaft = true;
            this.generateMonument = true;
            this.generateStronghold = true;
            this.generateTemple = true;
            this.generateVillage = true;
            this.generateFlatBedrock = false;
        }
    }
}
