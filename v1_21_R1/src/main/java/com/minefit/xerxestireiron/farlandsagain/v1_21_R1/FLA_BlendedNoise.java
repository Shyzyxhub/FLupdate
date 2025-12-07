package com.minefit.xerxestireiron.farlandsagain.v1_21_R1;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

/**
 * Version corrigée — constructeur Paper 1.21.4 OK
 */
public class FLA_BlendedNoise extends BlendedNoise {

    private final PerlinNoise minLimitNoise;
    private final PerlinNoise maxLimitNoise;
    private final PerlinNoise mainNoise;
    private final double xzScale;
    private final double yScale;
    private final double xzMainScale;
    private final double yMainScale;
    private final int cellWidth;
    private final int cellHeight;
    private final ConfigValues configValues;
    private final int lowX;
    private final int lowZ;
    private final int highX;
    private final int highZ;

    public FLA_BlendedNoise(
            PerlinNoise minLimitNoise,
            PerlinNoise maxLimitNoise,
            PerlinNoise mainNoise,
            NoiseSettings samplingSettings,
            int cellWidth,
            int cellHeight,
            RandomSource randomSource,
            ConfigValues configValues,
            int divisor
    ) {
        // 👉 Appel correct du constructeur Paper 1.21.4
        super(
                randomSource,
                684.412D * getDouble(samplingSettings, "xzScale"),
                684.412D * getDouble(samplingSettings, "yScale"),
                684.412D * getDouble(samplingSettings, "xzScale") / getDouble(samplingSettings, "xzFactor"),
                684.412D * getDouble(samplingSettings, "yScale") / getDouble(samplingSettings, "yFactor"),
                0.0D
        );

        this.minLimitNoise = minLimitNoise;
        this.maxLimitNoise = maxLimitNoise;
        this.mainNoise = mainNoise;

        this.xzScale = 684.412D * getDouble(samplingSettings, "xzScale");
        this.yScale = 684.412D * getDouble(samplingSettings, "yScale");
        this.xzMainScale = this.xzScale / getDouble(samplingSettings, "xzFactor");
        this.yMainScale = this.yScale / getDouble(samplingSettings, "yFactor");

        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.configValues = configValues;

        if (configValues != null) {
            this.highX = configValues.farLandsHighX;
            this.highZ = configValues.farLandsHighZ;
            this.lowX = configValues.farLandsLowX;
            this.lowZ = configValues.farLandsLowZ;
        } else {
            this.highX = Integer.MAX_VALUE;
            this.highZ = Integer.MAX_VALUE;
            this.lowX = Integer.MIN_VALUE;
            this.lowZ = Integer.MIN_VALUE;
        }
    }

    private static double getDouble(NoiseSettings settings, String field) {
        try {
            var m = settings.getClass().getMethod(field);
            Object r = m.invoke(settings);
            if (r instanceof Number n) return n.doubleValue();
        } catch (Throwable ignored) {}
        return 1.0D;
    }

    @SuppressWarnings("deprecation")
    public double calculateNoise(int x, int y, int z) {
        int cellX = Math.floorDiv(x, this.cellWidth);
        int cellY = Math.floorDiv(y, this.cellHeight);
        int cellZ = Math.floorDiv(z, this.cellWidth);

        double minAcc = 0.0D;
        double maxAcc = 0.0D;
        double mainAcc = 0.0D;
        double amp = 1.0D;

        int multiX = (x >= this.highX || x <= this.lowX) ? 3137706 : 1;
        int multiZ = (z >= this.highZ || z <= this.lowZ) ? 3137706 : 1;

        for (int i = 0; i < 8; ++i) {
            ImprovedNoise oct = this.mainNoise.getOctaveNoise(i);
            if (oct != null) {
                mainAcc += oct.noise(
                        PerlinNoise.wrap(cellX * this.xzMainScale * amp),
                        PerlinNoise.wrap(cellY * this.yMainScale * amp),
                        PerlinNoise.wrap(cellZ * this.xzMainScale * amp),
                        this.yMainScale * amp,
                        cellY * this.yMainScale * amp
                ) / amp;
            }
            amp /= 2.0D;
        }

        double blend = (mainAcc / 10.0D + 1.0D) / 2.0D;
        boolean useMin = blend < 1.0D;
        boolean useMax = blend > 0.0D;
        amp = 1.0D;

        for (int i = 0; i < 16; ++i) {
            double nx = PerlinNoise.wrap(cellX * this.xzScale * amp);
            double ny = PerlinNoise.wrap(cellY * this.yScale * amp);
            double nz = PerlinNoise.wrap(cellZ * this.xzScale * amp);
            double yl = this.yScale * amp;

            ImprovedNoise oct;
            if (useMin && (oct = this.minLimitNoise.getOctaveNoise(i)) != null)
                minAcc += oct.noise(nx * multiX, ny, nz * multiZ, yl, cellY * yl) / amp;
            if (useMax && (oct = this.maxLimitNoise.getOctaveNoise(i)) != null)
                maxAcc += oct.noise(nx * multiX, ny, nz * multiZ, yl, cellY * yl) / amp;

            amp /= 2.0D;
        }

        return Mth.clampedLerp(minAcc / 512.0D, maxAcc / 512.0D, blend) / 128.0D;
    }

    @VisibleForTesting
    public void parityConfigString(StringBuilder sb) {
        sb.append("BlendedNoise{minLimitNoise=");
        try { this.minLimitNoise.parityConfigString(sb); } catch (Throwable ignored) {}
        sb.append(", maxLimitNoise=");
        try { this.maxLimitNoise.parityConfigString(sb); } catch (Throwable ignored) {}
        sb.append(", mainNoise=");
        try { this.mainNoise.parityConfigString(sb); } catch (Throwable ignored) {}
        sb.append(String.format(
                ", xzScale=%.3f, yScale=%.3f, xzMainScale=%.3f, yMainScale=%.3f, cellWidth=%d, cellHeight=%d}",
                this.xzScale, this.yScale, this.xzMainScale, this.yMainScale, this.cellWidth, this.cellHeight
        ));
    }
}
