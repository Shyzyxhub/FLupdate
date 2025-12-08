package com.minefit.xerxestireiron.farlandsagain.v1_21_R1;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Wrapper léger pour détecter les options spécifiques Paper d'un monde.
 * Compatible Paper 1.21.4 - Utilise reflection pour éviter les dépendances directes.
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
                // Charger SpigotWorldConfig par reflection
                Class<?> swcClass = Class.forName("org.spigotmc.SpigotWorldConfig");
                Constructor<?> swcConstructor = swcClass.getConstructor(String.class);
                Object spigotConfig = swcConstructor.newInstance(worldName);

                // Essayer de charger PaperWorldConfig
                try {
                    Class<?> pwcClass = Class.forName("com.destroystokyo.paper.PaperWorldConfig");
                    Constructor<?> pwcConstructor = pwcClass.getConstructor(String.class, swcClass);
                    Object paperConfig = pwcConstructor.newInstance(worldName, spigotConfig);

                    // Récupérer generateFlatBedrock
                    try {
                        Field f = pwcClass.getDeclaredField("generateFlatBedrock");
                        f.setAccessible(true);
                        Object val = f.get(paperConfig);
                        if (val instanceof Boolean) flatBedrock = (Boolean) val;
                    } catch (NoSuchFieldException ignored) {
                        // Le champ n'existe pas ou a changé de nom
                    }
                } catch (ClassNotFoundException ignored) {
                    // PaperWorldConfig n'existe pas (peut-être supprimé dans Paper 1.21+)
                }
            } catch (Throwable ignored) {
                // Échec de reflection : on utilise les valeurs par défaut
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