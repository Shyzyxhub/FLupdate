package com.minefit.xerxestireiron.farlandsagain.v1_21_R1;

import com.minefit.xerxestireiron.farlandsagain.Messages;
import com.minefit.xerxestireiron.farlandsagain.utility.ReflectionHelper;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * LoadFarlands adapté pour Paper 1.21.4.
 * Reflection robuste pour trouver BlendedNoise et ses PerlinNoise.
 */
public class LoadFarlands {
    private final World world;
    private final ServerLevel nmsWorld;
    private final String worldName;
    private String originalGenName;
    private final Messages messages;
    private ChunkGenerator originalGenerator;
    private final ConfigurationSection worldConfig;
    private ServerChunkCache serverChunkCache;
    public final ConfigValues configValues;
    private boolean isPaper;

    public LoadFarlands(World world, ConfigurationSection worldConfig, boolean isPaper, String pluginName) {
        this.world = world;
        this.worldConfig = worldConfig;
        this.worldName = this.world.getName();
        this.isPaper = isPaper;
        this.nmsWorld = ((CraftWorld) world).getHandle();
        this.messages = new Messages(pluginName);
        this.configValues = new ConfigValues(this.worldName, this.worldConfig, this.isPaper);
        this.serverChunkCache = this.nmsWorld.getChunkSource();
        this.originalGenerator = this.serverChunkCache.getGenerator();
        this.originalGenName = this.originalGenerator.getClass().getSimpleName();
        modifyGenerator();
    }

    public void restoreGenerator() {
        // non implémenté pour le moment
    }

    public void modifyGenerator() {
        Environment environment = this.world.getEnvironment();
        boolean enabled = false;

        if (this.nmsWorld.isFlat()) {
            this.messages.providerFlat(this.worldName);
            return;
        } else if (!isRecognizedGenerator(environment, this.originalGenName)) {
            this.messages.unknownGenerator(this.worldName, this.originalGenName);
            return;
        } else {
            int divisor = (environment == Environment.THE_END) ? 8 : 1;

            NoiseBasedChunkGenerator noiseGen = (NoiseBasedChunkGenerator) this.originalGenerator;

            // FIX 1: Utiliser .value() au lieu de .get()
            WorldgenRandom.Algorithm alg = noiseGen.settings.value().getRandomSource();
            RandomSource randomSource = alg.newInstance(this.nmsWorld.getSeed());
            NoiseSettings noiseSettings = noiseGen.settings.value().noiseSettings();

            // FIX 2: Remplacer climateSampler() par reflection
            Object noiseSampler = null;
            try {
                // Essayer d'abord le champ "noiseRouter" (nom Mojang mapping)
                Field routerField = noiseGen.getClass().getDeclaredField("noiseRouter");
                routerField.setAccessible(true);
                noiseSampler = routerField.get(noiseGen);
            } catch (NoSuchFieldException e) {
                // Sinon, chercher dynamiquement un champ qui contient BlendedNoise
                for (Field f : noiseGen.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        Object val = f.get(noiseGen);
                        if (val != null) {
                            for (Field innerF : val.getClass().getDeclaredFields()) {
                                if (BlendedNoise.class.isAssignableFrom(innerF.getType())) {
                                    noiseSampler = val;
                                    break;
                                }
                            }
                        }
                        if (noiseSampler != null) break;
                    } catch (IllegalAccessException ignored) {}
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (noiseSampler == null) {
                this.messages.unknownNoise(worldName, "NoiseRouter not found");
                return;
            }

            try {
                // Recherche du BlendedNoise dans noiseSampler
                Field blendedNoiseField = null;
                for (Field f : noiseSampler.getClass().getDeclaredFields()) {
                    if (BlendedNoise.class.isAssignableFrom(f.getType())) {
                        f.setAccessible(true);
                        blendedNoiseField = f;
                        break;
                    }
                }

                if (blendedNoiseField == null) {
                    blendedNoiseField = ReflectionHelper.getField(noiseSampler.getClass(), "q", true);
                    blendedNoiseField.setAccessible(true);
                }

                BlendedNoise blendedNoise = (BlendedNoise) blendedNoiseField.get(noiseSampler);
                String blendedNoiseName = blendedNoise != null ? blendedNoise.getClass().getSimpleName() : "null";

                if (blendedNoise != null) {
                    // Trouver les PerlinNoise (min, max, main)
                    List<PerlinNoise> perlinList = new ArrayList<>();
                    List<Field> intFields = new ArrayList<>();

                    for (Field f : blendedNoise.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        Class<?> t = f.getType();
                        if (PerlinNoise.class.isAssignableFrom(t)) {
                            Object val = f.get(blendedNoise);
                            if (val instanceof PerlinNoise) perlinList.add((PerlinNoise) val);
                        } else if (t == int.class || t == Integer.class) {
                            intFields.add(f);
                        }
                    }

                    if (perlinList.size() >= 3 && intFields.size() >= 2) {
                        PerlinNoise minLimitNoise = perlinList.get(0);
                        PerlinNoise maxLimitNoise = perlinList.get(1);
                        PerlinNoise mainNoise = perlinList.get(2);

                        int cellWidth = intFields.get(0).getInt(blendedNoise);
                        int cellHeight = intFields.get(1).getInt(blendedNoise);

                        FLA_BlendedNoise newBlendedNoise = new FLA_BlendedNoise(
                                minLimitNoise, maxLimitNoise, mainNoise,
                                noiseSettings, cellWidth, cellHeight,
                                randomSource, this.configValues, divisor
                        );

                        ReflectionHelper.fieldSetter(blendedNoiseField, noiseSampler, newBlendedNoise);
                        enabled = true;
                    } else {
                        this.messages.unknownNoise(worldName, blendedNoiseName);
                        return;
                    }
                } else {
                    this.messages.unknownNoise(worldName, "null");
                    return;
                }
            } catch (Throwable t) {
                t.printStackTrace();
                enabled = false;
            }
        }

        if (enabled) {
            this.messages.enableSuccess(worldName);
        } else {
            this.messages.enableFailed(worldName);
        }
    }

    private boolean isRecognizedGenerator(Environment environment, String originalGenName) {
        return "ChunkGeneratorAbstract".equals(originalGenName) || "NoiseGenerator".equals(originalGenName);
    }
}