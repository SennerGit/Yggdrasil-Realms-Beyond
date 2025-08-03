package net.sen.yggdrasil.common.world.biomes.alfheimr;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.sen.yggdrasil.YggdrasilRealmsBeyond;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondWorldCarvers;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondTags;
import net.sen.yggdrasil.common.utils.ModUtils;

public class AlfheimrBiomes {


    public static final ResourceKey<Biome> TEST_BIOME = create("test_biome");

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverGetter = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(TEST_BIOME, biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultGen(featureGetter, carverGetter)).build());
    }

    // Defaults
    public static Biome.BiomeBuilder biomeWithDefaults(BiomeSpecialEffects.Builder biomeAmbience, MobSpawnSettings.Builder mobSpawnInfo, BiomeGenerationSettings.Builder biomeGenerationSettings) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(biomeAmbience.build())
                .mobSpawnSettings(mobSpawnInfo.build())
                .generationSettings(biomeGenerationSettings.build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE);
    }
    public static Biome.BiomeBuilder oceanWithDefaults(BiomeSpecialEffects.Builder biomeAmbience, MobSpawnSettings.Builder mobSpawnInfo, BiomeGenerationSettings.Builder biomeGenerationSettings) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(biomeAmbience.build())
                .mobSpawnSettings(mobSpawnInfo.build())
                .generationSettings(biomeGenerationSettings.build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE);
    }

    public static BiomeGenerationSettings.Builder defaultGen(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);
        commonFeatures(biome);
//        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.TREE_BLACK_BIRCH_KEY);
//        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.TREE_BUR_OAK_KEY);
//        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.TREE_WHITE_OAK_KEY);
//        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.ALFHEIMR_MAGIC_GRASS_PLACED_KEY);
//        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.ALFHEIMR_MAGIC_GRASS_SINGLE_PLACED_KEY);
//        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.ALFHEIMR_MAGIC_GRASS_PATCH_PLACED_KEY);
//        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.ALFHEIMR_FLOWERS_KEY);
//
//        biome.addFeature(GenerationStep.Decoration.RAW_GENERATION, ModDecorPlacements.PLACED_ALFHEIMR_ROCKS_KEY);
//        biome.addFeature(GenerationStep.Decoration.RAW_GENERATION, ModDecorPlacements.PLACED_ALFHEIMR_STICKS_KEY);

        return biome;
    }

    public static BiomeGenerationSettings.Builder defaultGenSettingBuilder(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

        BiomeDefaultFeatures.addDefaultSoftDisks(biome);
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
        BiomeDefaultFeatures.addSurfaceFreezing(biome);
        addCaves(biome);
        addSmallStoneClusters(biome);

        return biome;
    }

    public static BiomeSpecialEffects.Builder defaultAmbientBuilder() {
        BiomeSpecialEffects.Builder biomeBuilder = new BiomeSpecialEffects.Builder()
                .fogColor(0x147229)
                .waterColor(0x49e8c0)
                .waterFogColor(0x28ca82)
                .skyColor(0x409cc9)
                .foliageColorOverride(0x147229)
                .grassColorOverride(0x3afc6d)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS) // We should probably change it
                .backgroundMusic(Musics.createGameMusic(SoundEvents.AMBIENT_WARPED_FOREST_LOOP));

        return biomeBuilder;
    }

    public static MobSpawnSettings.Builder defaultMobSpawning() {
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

        spawnInfo.creatureGenerationProbability(0.1f);

//        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.RHINO.get(), 12, 4, 4));

        return spawnInfo;
    }

    public static MobSpawnSettings.Builder oceanMobSpawning() {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, 20, 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 20, 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 20, 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 20, 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 20, 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 20, 4, 8));

        return spawns;
    }

    public static BiomeGenerationSettings.Builder globalOverworldGeneration(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(biome);
        BiomeDefaultFeatures.addDefaultCrystalFormations(biome);
        BiomeDefaultFeatures.addDefaultMonsterRoom(biome);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(biome);
        BiomeDefaultFeatures.addDefaultSprings(biome);
        BiomeDefaultFeatures.addSurfaceFreezing(biome);

        return biome;
    }

    //Caves!
    public static void addCaves(BiomeGenerationSettings.Builder biome) {
        biome.addCarver(GenerationStep.Carving.AIR, YggdrasilRealmsBeyondWorldCarvers.ALFHEIMR_CAVE);
        addLegacyOres(biome);
    }

    public static void addSmallStoneClusters(BiomeGenerationSettings.Builder biome) {
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModDecorPlacements.PLACED_ALFHEIMR_SMALL_ANDESITE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModDecorPlacements.PLACED_ALFHEIMR_SMALL_DIORITE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModDecorPlacements.PLACED_ALFHEIMR_SMALL_GRANITE_KEY);
    }

    public static void commonFeatures(BiomeGenerationSettings.Builder biome) {
        commonFeaturesWithoutBuildings(biome);
    }

    public static void commonFeaturesWithoutBuildings(BiomeGenerationSettings.Builder biome) {
    }

    public static void addLegacyOres(BiomeGenerationSettings.Builder biome) {
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModOrePlacements.PLACED_ALFHEIMR_COAL_ORE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModOrePlacements.PLACED_ALFHEIMR_IRON_ORE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModOrePlacements.PLACED_ALFHEIMR_GOLD_ORE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModOrePlacements.PLACED_ALFHEIMR_REDSTONE_ORE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModOrePlacements.PLACED_ALFHEIMR_DIAMOND_ORE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModOrePlacements.PLACED_ALFHEIMR_LAPIS_ORE_KEY);
//        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModOrePlacements.PLACED_ALFHEIMR_COPPER_ORE_KEY);
    }

    private static ResourceKey<Biome> create(String name) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(YggdrasilRealmsBeyond.MODID, name));
    }
}
