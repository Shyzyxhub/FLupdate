package com.minefit.xerxestireiron.farlandsagain;

import java.util.List;
import org.bukkit.Bukkit;

/**
 * Classe utilitaire pour déterminer la version du serveur.
 * Compatible Paper 1.21.4 et versions modernes.
 */
public class ServerVersion {

    private final FarLandsAgain plugin;
    private final String nmsVersion;
    private final String major;
    private final String minor;
    private final String revision;

    public ServerVersion(FarLandsAgain instance) {
        this.plugin = instance;

        // Récupération du package name (peut ne plus contenir de version NMS sur Paper moderne)
        String packageName = this.plugin.getServer().getClass().getPackage().getName();
        String lastPart = packageName.substring(packageName.lastIndexOf(".") + 1);

        // Vérifier si on a un format NMS classique (v1_21_R1) ou non
        if (lastPart.matches("v\\d+_\\d+_R\\d+")) {
            // Format classique détecté
            this.nmsVersion = lastPart;
            String[] vn = this.nmsVersion.split("_");
            this.major = vn[0];     // v1
            this.minor = vn[1];     // 21
            this.revision = vn[2];  // R1
        } else {
            // Paper moderne : extraire depuis Bukkit.getVersion()
            String version = Bukkit.getVersion(); // ex: "git-Paper-232 (MC: 1.21.4)"
            String mcVersion = extractMinecraftVersion(version);

            if (mcVersion != null) {
                String[] parts = mcVersion.split("\\.");
                this.major = parts.length > 0 ? parts[0] : "1";
                this.minor = parts.length > 1 ? parts[1] : "21";
                this.revision = parts.length > 2 ? parts[2] : "4";
                this.nmsVersion = "v" + this.major + "_" + this.minor + "_R1";
            } else {
                // Fallback par défaut
                this.major = "1";
                this.minor = "21";
                this.revision = "4";
                this.nmsVersion = "v1_21_R1";
            }

            plugin.getLogger().warning("NMS version classique non détectée. Version reconstruite: " + this.nmsVersion);
        }
    }

    /**
     * Extrait la version Minecraft depuis une chaîne comme "git-Paper-232 (MC: 1.21.4)"
     */
    private String extractMinecraftVersion(String versionString) {
        try {
            int mcIndex = versionString.indexOf("MC: ");
            if (mcIndex != -1) {
                int start = mcIndex + 4;
                int end = versionString.indexOf(")", start);
                if (end != -1) {
                    return versionString.substring(start, end);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Impossible d'extraire la version MC: " + e.getMessage());
        }
        return null;
    }

    /**
     * Vérifie si la version NMS est compatible avec une liste de versions supportées.
     */
    public boolean compatibleVersion(List<String> list) {
        return list.contains(this.nmsVersion);
    }

    public String getMajor() {
        return this.major;
    }

    public String getMinor() {
        return this.minor;
    }

    public String getRevision() {
        return this.revision;
    }

    public String getNMSVersion() {
        return this.nmsVersion;
    }
}
