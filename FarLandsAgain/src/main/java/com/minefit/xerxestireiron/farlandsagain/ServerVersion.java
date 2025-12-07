package com.minefit.xerxestireiron.farlandsagain;

import java.util.List;

/**
 * Classe utilitaire pour déterminer la version NMS du serveur.
 * Compatible Paper 1.21.4.
 */
public class ServerVersion {

    private final FarLandsAgain plugin;
    private final String nmsVersion;
    private final String major;
    private final String minor;
    private final String revision;

    public ServerVersion(FarLandsAgain instance) {
        this.plugin = instance;
        String name = this.plugin.getServer().getClass().getPackage().getName();
        this.nmsVersion = name.substring(name.lastIndexOf(".") + 1); // ex: v1_21_R1

        String[] vn = this.nmsVersion.split("_");
        this.major = vn[0];     // ex: v1
        this.minor = vn[1];     // ex: 21
        this.revision = vn[2];  // ex: R1
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
