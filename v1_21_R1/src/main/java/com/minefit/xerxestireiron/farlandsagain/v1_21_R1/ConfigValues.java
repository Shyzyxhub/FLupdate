package com.minefit.xerxestireiron.farlandsagain.v1_21_R1;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Classe de configuration pour un monde, compatible Paper 1.21.4
 */
public class ConfigValues {

    private final ConfigurationSection worldConfig;
    public final PaperSpigot paperSpigot;

    // Bornes des Far Lands pour ce monde
    public final int farLandsLowX;
    public final int farLandsLowZ;
    public final int farLandsHighX;
    public final int farLandsHighZ;

    /**
     * Crée les valeurs de configuration du monde.
     *
     * @param worldName nom du monde
     * @param worldConfig section de configuration Bukkit pour ce monde
     * @param isPaper   true si le serveur est Paper
     */
    public ConfigValues(String worldName, ConfigurationSection worldConfig, boolean isPaper) {
        this.worldConfig = worldConfig;

        // Initialisation du support Paper/Spigot
        this.paperSpigot = new PaperSpigot(worldName, isPaper);

        // Récupération des limites des Far Lands depuis la config
        this.farLandsLowX = this.worldConfig.getInt("lowX", -12550824);
        this.farLandsLowZ = this.worldConfig.getInt("lowZ", -12550824);
        this.farLandsHighX = this.worldConfig.getInt("highX", 12550824);
        this.farLandsHighZ = this.worldConfig.getInt("highZ", 12550824);
    }
}
