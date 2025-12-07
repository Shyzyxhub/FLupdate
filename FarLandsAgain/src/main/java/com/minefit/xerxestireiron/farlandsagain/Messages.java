package com.minefit.xerxestireiron.farlandsagain;

import java.util.logging.Logger;

/**
 * Classe utilitaire pour logguer les messages du plugin FarLandsAgain.
 * Compatible Paper 1.21.4.
 */
public class Messages {

    private final String pluginName;
    private final Logger logger = Logger.getLogger("Minecraft");

    public Messages(String pluginName) {
        this.pluginName = pluginName;
    }

    public void unknownGenerator(String worldName, String generatorName) {
        logger.info("[" + pluginName + " Error] The world '" + worldName + "' does not have a recognized generator.");
        logger.info("[" + pluginName + " Error] A custom generator may already be in place or Mojang changed something.");
        logger.info("[" + pluginName + " Error] The generator detected is: '" + generatorName + "'.");
        logger.info("[" + pluginName + " Error] For safety, FarLandsAgain will not be enabled on this world.");
    }

    public void unknownEnvironment(String worldName, String environment) {
        logger.info("[" + pluginName + " Error] The world '" + worldName + "' is not a recognized environment.");
        logger.info("[" + pluginName + " Error] FarLandsAgain will not be enabled on this world.");
    }

    public void incompatibleVersion() {
        logger.info("[" + pluginName + " Error] This version of Minecraft is not supported. Disabling plugin.");
    }

    public void enableSuccess(String worldName) {
        logger.info("[" + pluginName + " Success] The world '" + worldName + "' will have Far Lands!");
    }

    public void enableFailed(String worldName) {
        logger.info("[" + pluginName + " Error] Something went wrong enabling FarLandsAgain on world '" + worldName + "'.");
    }

    public void pluginReady() {
        logger.info("[" + pluginName + "] Everything is ready to go!");
    }

    public void pluginDisable() {
        logger.info("[" + pluginName + "] " + pluginName + " now disabled.");
    }

    public void alreadyEnabled(String worldName) {
        logger.info("[" + pluginName + " Success] FarLandsAgain appears to already be enabled for this world.");
    }

    public void restoreFailed(String worldName) {
        logger.info("[" + pluginName + " Error] Something went wrong while restoring the original world generation.");
    }

    public void providerFlat(String worldName) {
        logger.info("[" + pluginName + " Error] Flatlands generator detected for '" + worldName + "'.");
        logger.info("[" + pluginName + " Error] Far Lands do not generate in flat worlds.");
    }

    public void unknownNoise(String worldName, String noiseName) {
        logger.info("[" + pluginName + " Error] The world '" + worldName + "' does not have a recognized noise generator.");
        logger.info("[" + pluginName + " Error] The original may have been modified, replaced or Mojang changed something.");
        logger.info("[" + pluginName + " Error] The noise generator detected is: '" + noiseName + "'.");
        logger.info("[" + pluginName + " Error] To avoid possible conflicts, FarLandsAgain will not be enabled on this world.");
    }
}
