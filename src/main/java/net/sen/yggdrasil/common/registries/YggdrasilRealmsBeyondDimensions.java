package net.sen.yggdrasil.common.registries;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.sen.yggdrasil.common.world.dimension.alfheimr.AlfheimrDimension;

public class YggdrasilRealmsBeyondDimensions {
    public static void bootstrapNoise(BootstrapContext<NoiseGeneratorSettings> context) {
        AlfheimrDimension.bootstrapNoise(context);
    }
    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        AlfheimrDimension.bootstrapType(context);
    }
    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        AlfheimrDimension.bootstrapStem(context);
    }
}
