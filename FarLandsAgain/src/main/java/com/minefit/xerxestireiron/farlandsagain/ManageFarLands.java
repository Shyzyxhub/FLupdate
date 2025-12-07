package com.minefit.xerxestireiron.farlandsagain;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Gère le Far Lands pour un monde donné, selon la version NMS du serveur.
 */
public class ManageFarLands {

    private final FarLandsAgain plugin;
    private final World world;

    // Instances NMS pour chaque version supportée
    private com.minefit.xerxestireiron.farlandsagain.v1_21_R1.LoadFarlands LF21R1; // <-- ajouté pour 1.21.4

    public ManageFarLands(World world, FarLandsAgain instance) {
        this.plugin = instance;
        this.world = world;
        ConfigurationSection worldConfig = this.plugin.getConfig()
                .getConfigurationSection("worlds." + this.world.getName());

        switch (this.plugin.version) {
            case "v1_21_R1": // <-- support ajouté pour Paper 1.21.4
                this.LF21R1 = new com.minefit.xerxestireiron.farlandsagain.v1_21_R1.LoadFarlands(
                        this.world, worldConfig, this.plugin.isPaper(), this.plugin.getName());
                break;
        }
    }

    /**
     * Restaure les générateurs originaux lors du disable.
     */
    public void restoreGenerator() {
        switch (this.plugin.version) {
            case "v1_21_R1": // <-- support ajouté pour Paper 1.21.4
                if (LF21R1 != null) LF21R1.restoreGenerator();
                break;
        }
    }
}
