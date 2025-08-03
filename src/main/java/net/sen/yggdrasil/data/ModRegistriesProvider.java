package net.sen.yggdrasil.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondBiomes;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondDimensions;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondWorldCarvers;
import net.sen.yggdrasil.common.utils.ModUtils;
import net.sen.yggdrasil.data.world.*;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class ModRegistriesProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, YggdrasilRealmsBeyondBiomes::bootstrap)
            .add(Registries.CONFIGURED_CARVER, YggdrasilRealmsBeyondWorldCarvers::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, YggdrasilRealmsBeyondConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, YggdrasilRealmsBeyondPlacedFeatures::bootstrap)
            .add(Registries.NOISE_SETTINGS, YggdrasilRealmsBeyondDimensions::bootstrapNoise)
            .add(Registries.DIMENSION_TYPE, YggdrasilRealmsBeyondDimensions::bootstrapType)
            .add(Registries.LEVEL_STEM, YggdrasilRealmsBeyondDimensions::bootstrapStem)
;

    public ModRegistriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Collections.singleton(ModUtils.getModId()));
    }
}
