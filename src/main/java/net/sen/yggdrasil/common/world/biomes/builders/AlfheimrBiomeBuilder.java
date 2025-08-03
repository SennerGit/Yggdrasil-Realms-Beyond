package net.sen.yggdrasil.common.world.biomes.builders;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondDimensions;
import net.sen.yggdrasil.common.world.biomes.alfheimr.AlfheimrBiomes;
import net.sen.yggdrasil.common.world.dimension.alfheimr.AlfheimrDimension;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

//https://github.com/Tropicraft/Tropicraft/blob/1.20.1/src/main/java/net/tropicraft/core/common/dimension/biome/TropicraftBiomeBuilder.java#L74
public class AlfheimrBiomeBuilder {
    private static final MultiNoiseBiomeSourceParameterList.Preset PRESET = registerMultiNoisePreset(AlfheimrDimension.ALFHEIMR_ID, new MultiNoiseBiomeSourceParameterList.Preset.SourceProvider() {
        @Override
        public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> function) {
            ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> points = ImmutableList.builder();
            new AlfheimrBiomeBuilder().addBiomes((point, key) -> points.add(Pair.of(point, function.apply(key))));
            return new Climate.ParameterList<>(points.build());
        }
    });

    private static final float VALLEY_SIZE = 0.05F;
    private static final float LOW_START = 0.26666668F;
    public static final float HIGH_START = 0.4F;
    private static final float HIGH_END = 0.93333334F;
    private static final float PEAK_SIZE = 0.1F;
    public static final float PEAK_START = 0.56666666F;
    private static final float PEAK_END = 0.7666667F;
    public static final float NEAR_INLAND_START = -0.11F;
    public static final float MID_INLAND_START = 0.03F;
    public static final float FAR_INLAND_START = 0.3F;
    public static final float EROSION_INDEX_1_START = -0.78F;
    public static final float EROSION_INDEX_2_START = -0.375F;

    protected final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);

    /* Terminology:
        Continentalness: Low to generate near coasts, far to generate away from coasts
        Erosion: Low is hilly terrain, high is flat terrain
     */

    // 0 is the coldest, 4 is the hottest
    private final Climate.Parameter[] temperatures = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.45F),
            Climate.Parameter.span(-0.45F, -0.15F),
            Climate.Parameter.span(-0.15F, 0.2F),
            Climate.Parameter.span(0.2F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };

    // 0 is the driest, 4 is the wettest
    private final Climate.Parameter[] humidities = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.35F),
            Climate.Parameter.span(-0.35F, -0.1F),
            Climate.Parameter.span(-0.1F, 0.1F),
            Climate.Parameter.span(0.1F, 0.3F),
            Climate.Parameter.span(0.3F, 1.0F)
    };

    // 0 is the most shattered, 6 is the most flat
    private final Climate.Parameter[] erosions = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.78F),
            Climate.Parameter.span(-0.78F, -0.375F),
            Climate.Parameter.span(-0.375F, -0.2225F),
            Climate.Parameter.span(-0.2225F, 0.05F),
            Climate.Parameter.span(0.05F, 0.45F),
            Climate.Parameter.span(0.45F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };

    protected static final Climate.Parameter COMMON_RARENESS_RANGE = Climate.Parameter.span(-1.0F, 0.35F);
    protected static final Climate.Parameter RARE_RARENESS_RANGE = Climate.Parameter.span(0.35F, 1.0F);

    public static final ResourceKey<MultiNoiseBiomeSourceParameterList> PARAMETER_LIST = ResourceKey.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, AlfheimrDimension.ALFHEIMR_ID);

//    private final Climate.Parameter islandContinentalness = Climate.Parameter.span(-1.1F, -0.92F);
//    private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.92F, -0.19F);
//    private final Climate.Parameter landContinentalness = Climate.Parameter.span(-0.1F, 1.0F);
//
//    private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.1F);
//    private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.1F, 0.03F);
//    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
//    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
//    private final Climate.Parameter fullRange = Climate.Parameter.span(-1.0F, 1.0F);

    protected final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
    protected final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);
    protected final Climate.Parameter mushroomFieldsContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
    protected final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
    protected final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
    protected final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
    protected final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
    protected final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
    protected final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
    protected final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);

//    private final Climate.Parameter wet = Climate.Parameter.span(humidities[2], humidities[4]);
//    private final Climate.Parameter lessWet = Climate.Parameter.span(humidities[0], humidities[1]);
//    private final Climate.Parameter mostWet = Climate.Parameter.span(humidities[3], humidities[4]);
//
//    private final Climate.Parameter coldTemperature = Climate.Parameter.span(temperatures[0], humidities[1]);
//    private final Climate.Parameter warmTemperature = Climate.Parameter.span(temperatures[1], humidities[3]);
//    private final Climate.Parameter hotTemperature = Climate.Parameter.span(temperatures[3], humidities[4]);

//    private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_DEEP_FROZEN_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_COLD_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_LUKEWARM_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_WARM_OCEAN},
//            {AlfheimrBiomes.ALFHEIMR_FROZEN_OCEAN,AlfheimrBiomes.ALFHEIMR_COLD_OCEAN,AlfheimrBiomes.ALFHEIMR_OCEAN,AlfheimrBiomes.ALFHEIMR_LUKEWARM_OCEAN,AlfheimrBiomes.ALFHEIMR_WARM_OCEAN}
//    };
//
//    private final ResourceKey<Biome>[][] CORRUPTION_OCEAN = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_DEEP_FROZEN_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_COLD_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_LUKEWARM_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_WARM_CORRUPTION_OCEAN},
//            {AlfheimrBiomes.ALFHEIMR_FROZEN_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_COLD_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_LUKEWARM_CORRUPTION_OCEAN,AlfheimrBiomes.ALFHEIMR_WARM_CORRUPTION_OCEAN}
//    };
//
//    private final ResourceKey<Biome>[][] CURSED_OCEAN = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_DEEP_FROZEN_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_COLD_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_LUKEWARM_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_WARM_CURSED_OCEAN},
//            {AlfheimrBiomes.ALFHEIMR_FROZEN_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_COLD_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_LUKEWARM_CURSED_OCEAN,AlfheimrBiomes.ALFHEIMR_WARM_CURSED_OCEAN}
//    };
//
//    private final ResourceKey<Biome>[][] ENCHANTED_OCEAN = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_DEEP_FROZEN_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_COLD_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_LUKEWARM_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_DEEP_WARM_ENCHANTED_OCEAN},
//            {AlfheimrBiomes.ALFHEIMR_FROZEN_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_COLD_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_LUKEWARM_ENCHANTED_OCEAN,AlfheimrBiomes.ALFHEIMR_WARM_ENCHANTED_OCEAN}
//    };
//
//    private final ResourceKey<Biome>[][] ISLAND_BIOMES = new ResourceKey[][]{
//            {null, null, null, null, null},
//            {null, null, null, null, null},
//            {AlfheimrBiomes.ALFHEIMR_MAGIC_VALLEY, AlfheimrBiomes.ALFHEIMR_MAGIC_VALLEY, AlfheimrBiomes.ALFHEIMR_MAGIC_VALLEY, AlfheimrBiomes.ALFHEIMR_MAGIC_VALLEY, AlfheimrBiomes.ALFHEIMR_MAGIC_VALLEY},
//            {AlfheimrBiomes.ALFHEIMR_TROPICS, AlfheimrBiomes.ALFHEIMR_TROPICS, AlfheimrBiomes.ALFHEIMR_TROPICS, AlfheimrBiomes.ALFHEIMR_TROPICS, AlfheimrBiomes.ALFHEIMR_TROPICS},
//            {AlfheimrBiomes.ALFHEIMR_MAGIC_TROPICS, AlfheimrBiomes.ALFHEIMR_MAGIC_TROPICS, AlfheimrBiomes.ALFHEIMR_MAGIC_TROPICS, AlfheimrBiomes.ALFHEIMR_MAGIC_TROPICS, AlfheimrBiomes.ALFHEIMR_MAGIC_TROPICS}
//    };
//
//    private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_SNOWY_PLAINS, AlfheimrBiomes.ALFHEIMR_SNOWY_PLAINS, AlfheimrBiomes.ALFHEIMR_SNOWY_PLAINS, AlfheimrBiomes.ALFHEIMR_SNOW_FOREST, AlfheimrBiomes.ALFHEIMR_SNOW_FOREST},
//            {AlfheimrBiomes.ALFHEIMR_PLAINS, AlfheimrBiomes.ALFHEIMR_PLAINS, AlfheimrBiomes.ALFHEIMR_FOREST, AlfheimrBiomes.ALFHEIMR_DENSE_FOREST, AlfheimrBiomes.ALFHEIMR_HAUNTED_FORESTS},
//            {AlfheimrBiomes.ALFHEIMR_FLOWER_PLAINS, AlfheimrBiomes.ALFHEIMR_PLAINS, AlfheimrBiomes.ALFHEIMR_FOREST, AlfheimrBiomes.ALFHEIMR_ENCHANTED_FOREST, AlfheimrBiomes.ALFHEIMR_GLISTENING_FORESTS},
//            {AlfheimrBiomes.ALFHEIMR_SAVANNA, AlfheimrBiomes.ALFHEIMR_SAVANNA, AlfheimrBiomes.ALFHEIMR_FOREST, AlfheimrBiomes.ALFHEIMR_JUNGLE, AlfheimrBiomes.ALFHEIMR_JUNGLE},
//            {AlfheimrBiomes.ALFHEIMR_DESERT, AlfheimrBiomes.ALFHEIMR_DESERT, AlfheimrBiomes.ALFHEIMR_DESERT, AlfheimrBiomes.ALFHEIMR_DESERT, AlfheimrBiomes.ALFHEIMR_DESERT}
//    };
//
//    private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_FROZEN_JUNGLE, AlfheimrBiomes.ALFHEIMR_SNOW_GROVE, AlfheimrBiomes.ALFHEIMR_SNOW_FOREST, null, null},
//            {null, null, null, AlfheimrBiomes.ALFHEIMR_DARK_ENCHANTED_FOREST, null},
//            {AlfheimrBiomes.ALFHEIMR_MYSTICAL_PLAINS, null, AlfheimrBiomes.ALFHEIMR_DENSE_FOREST, null, null},
//            {null, null, AlfheimrBiomes.ALFHEIMR_PLAINS, null, null},
//            {null, null, null, null, null}
//    };
//
//    private final ResourceKey<Biome>[][] SWAMP_BIOMES = new ResourceKey[][]{
//            {null, null, null, null, null},
//            {null, null, null, null, null},
//            {null, null, null, null, null},
//            {null, null, null, null, null},
//            {null, null, null, null, null}
//    };
//
//    private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_SNOWY_PLAINS, AlfheimrBiomes.ALFHEIMR_SNOWY_PLAINS, AlfheimrBiomes.ALFHEIMR_SNOWY_PLAINS, AlfheimrBiomes.ALFHEIMR_SNOW_FOREST, AlfheimrBiomes.ALFHEIMR_SNOW_FOREST},
//            {AlfheimrBiomes.ALFHEIMR_MEADOWS, AlfheimrBiomes.ALFHEIMR_MEADOWS, AlfheimrBiomes.ALFHEIMR_FOREST, AlfheimrBiomes.ALFHEIMR_DENSE_FOREST, AlfheimrBiomes.ALFHEIMR_GLISTENING_FORESTS},
//            {AlfheimrBiomes.ALFHEIMR_MEADOWS, AlfheimrBiomes.ALFHEIMR_MEADOWS, AlfheimrBiomes.ALFHEIMR_MEADOWS, AlfheimrBiomes.ALFHEIMR_MEADOWS, AlfheimrBiomes.ALFHEIMR_DARK_ENCHANTED_FOREST},
//            {AlfheimrBiomes.ALFHEIMR_SAVANNA, AlfheimrBiomes.ALFHEIMR_SAVANNA, AlfheimrBiomes.ALFHEIMR_FOREST, AlfheimrBiomes.ALFHEIMR_FOREST, AlfheimrBiomes.ALFHEIMR_JUNGLE},
//            {AlfheimrBiomes.ALFHEIMR_BADLANDS, AlfheimrBiomes.ALFHEIMR_BADLANDS, AlfheimrBiomes.ALFHEIMR_BADLANDS, AlfheimrBiomes.ALFHEIMR_BADLANDS, AlfheimrBiomes.ALFHEIMR_BADLANDS}
//    };
//
//    private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_FROZEN_JUNGLE, AlfheimrBiomes.ALFHEIMR_FROZEN_GROVE, AlfheimrBiomes.ALFHEIMR_SNOW_GROVE, AlfheimrBiomes.ALFHEIMR_SNOW_GROVE, null},
//            {null, null, AlfheimrBiomes.ALFHEIMR_MEADOWS, AlfheimrBiomes.ALFHEIMR_MEADOWS, null},
//            {null, null, AlfheimrBiomes.ALFHEIMR_FOREST, null, null},
//            {null, null, null, null, null},
//            {null, null, null, null, null}
//    };
//
//    private final ResourceKey<Biome>[][] EXTREME_HILLS  = new ResourceKey[][]{
//            {AlfheimrBiomes.ALFHEIMR_HOLLOW_HILLS, AlfheimrBiomes.ALFHEIMR_HOLLOW_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS},
//            {AlfheimrBiomes.ALFHEIMR_HOLLOW_HILLS, AlfheimrBiomes.ALFHEIMR_HOLLOW_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS},
//            {AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS, AlfheimrBiomes.ALFHEIMR_KINGDOM_HILLS},
//            {null, null, null, null, null},
//            {null, null, null, null, null}
//    };

    public static void bootstrap(final BootstrapContext<MultiNoiseBiomeSourceParameterList> context) {
        context.register(PARAMETER_LIST, new MultiNoiseBiomeSourceParameterList(PRESET, context.lookup(Registries.BIOME)));
    }

    private static MultiNoiseBiomeSourceParameterList.Preset registerMultiNoisePreset(ResourceLocation id, MultiNoiseBiomeSourceParameterList.Preset.SourceProvider sourceProvider) {
        MultiNoiseBiomeSourceParameterList.Preset preset = new MultiNoiseBiomeSourceParameterList.Preset(id, sourceProvider);
        Map<ResourceLocation, MultiNoiseBiomeSourceParameterList.Preset> byName = new Object2ObjectOpenHashMap<>(MultiNoiseBiomeSourceParameterList.Preset.BY_NAME);
        byName.put(id, preset);
        MultiNoiseBiomeSourceParameterList.Preset.BY_NAME = Map.copyOf(byName);
        return preset;
    }

    public void addBiomes(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper) {
        this.addOffCoastBiomes(mapper);
        this.addInlandBiomes(mapper);
        this.addUndergroundBiomes(mapper);
    }

    public void addOffCoastBiomes(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper) {
        for (int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter temperature = this.temperatures[i];

            for (int j = 0; j < this.humidities.length; ++j)
            {
                Climate.Parameter humidity = this.humidities[j];
//                ResourceKey<Biome> islandBiomeBOP = this.pickIslandBiome(i, j);

//                this.addSurfaceBiome(mapper, temperature, humidity, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, islandBiomeBOP);
            }

//            this.addSurfaceBiome(mapper, temperature, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0f, this.OCEANS[0][i]);
//            this.addSurfaceBiome(mapper, temperature, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0f, this.OCEANS[1][i]);
        }
    }

    public void addInlandBiomes(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper) {
        /*
            Weirdness ranges map to specific slices in a repeating triangle wave fashion.
                   PEAKS                           PEAKS
               HIGH     HIGH                   HIGH     HIGH
            MID             MID             MID             MID
                               LOW       LOW
                                  VALLEYS
         */

        // First cycle
//        this.addMidSlice(mapper, Climate.Parameter.span(-1.0F, -0.93333334F));
//        this.addHighSlice(mapper, Climate.Parameter.span(-0.93333334F, -0.7666667F));
//        this.addPeaks(mapper, Climate.Parameter.span(-0.7666667F, -0.56666666F));
//        this.addHighSlice(mapper, Climate.Parameter.span(-0.56666666F, -0.4F));
//        this.addMidSlice(mapper, Climate.Parameter.span(-0.4F, -0.26666668F));
//        this.addLowSlice(mapper, Climate.Parameter.span(-0.26666668F, -0.05F));
//        this.addValleys(mapper, Climate.Parameter.span(-0.05F, 0.05F));
//        this.addLowSlice(mapper, Climate.Parameter.span(0.05F, 0.26666668F));
//        this.addMidSlice(mapper, Climate.Parameter.span(0.26666668F, 0.4F));
//
////        // Second cycle is truncated
//        this.addHighSlice(mapper, Climate.Parameter.span(0.4F, 0.56666666F));
//        this.addPeaks(mapper, Climate.Parameter.span(0.56666666F, 0.7666667F));
//        this.addHighSlice(mapper, Climate.Parameter.span(0.7666667F, 0.93333334F));
//        this.addMidSlice(mapper, Climate.Parameter.span(0.93333334F, 1.0F));
    }

//    protected void addPeaks(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter weirdness) {
//        for (int i = 0; i < this.temperatures.length; ++i) {
//            Climate.Parameter temperature = this.temperatures[i];
//
//            for (int j = 0; j < this.humidities.length; ++j) {
//                Climate.Parameter humidity = this.humidities[j];
//
//                ResourceKey<Biome> middleBiome = this.pickMiddleBiome(i, j, weirdness);
//                ResourceKey<Biome> middleBadlandsOrSlopeBiome = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
//
//                ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(i, j, weirdness);
//                ResourceKey<Biome> extremeHillsBiome = this.pickExtremeHillsBiome(i, j, weirdness);
//                ResourceKey<Biome> shatteredBiome = this.maybePickShatteredBiome(i, j, weirdness, extremeHillsBiome);
//                ResourceKey<Biome> peakBiome = this.pickPeakBiome(i, j, weirdness);
//
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBadlandsOrSlopeBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, peakBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, extremeHillsBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
//            }
//        }
//    }

//    protected void addHighSlice(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter weirdness) {
//        for (int i = 0; i < this.temperatures.length; ++i)
//        {
//            Climate.Parameter temperature = this.temperatures[i];
//
//            for (int j = 0; j < this.humidities.length; ++j)
//            {
//                Climate.Parameter humidity = this.humidities[j];
//
//                ResourceKey<Biome> middleBiome                = this.pickMiddleBiome(i, j, weirdness);
//                ResourceKey<Biome> middleBadlandsOrSlopeBiome     = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
//
//                ResourceKey<Biome> plateauBiome            = this.pickPlateauBiome(i, j, weirdness);
//                ResourceKey<Biome> extremeHillsBiome       = this.pickExtremeHillsBiome(i, j, weirdness);
//                ResourceKey<Biome> shatteredBiome             = this.maybePickShatteredBiome(i, j, weirdness, middleBiome);
//                ResourceKey<Biome> slopeBiome              = this.pickSlopeBiome(i, j, weirdness);
//                ResourceKey<Biome> peakBiome               = this.pickPeakBiome(i, j, weirdness);
//
//                this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.nearInlandContinentalness, this.erosions[0], weirdness, 0.0F, slopeBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.nearInlandContinentalness, this.erosions[1], weirdness, 0.0F, middleBadlandsOrSlopeBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, slopeBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, extremeHillsBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
//            }
//        }
//    }

//    protected void addMidSlice(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter weirdness) {
////        this.addSurfaceBiome(mapper, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_STONY_SHORE);
//
//        for (int i = 0; i < this.temperatures.length; ++i)
//        {
//            Climate.Parameter temperature = this.temperatures[i];
//
//            for (int j = 0; j < this.humidities.length; ++j)
//            {
//                Climate.Parameter humidity = this.humidities[j];
//
//                ResourceKey<Biome> middleBiome                  = this.pickMiddleBiome(i, j, weirdness);
//                ResourceKey<Biome> middleBadlandsOrSlopeBiome       = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
//
//                ResourceKey<Biome> extremeHillsBiome       = this.pickExtremeHillsBiome(i, j, weirdness);
//                ResourceKey<Biome> plateauBiome            = this.pickPlateauBiome(i, j, weirdness);
//                ResourceKey<Biome> beachBiome                 = this.pickBeachBiome(i, j);
//                ResourceKey<Biome> shatteredBiome             = this.maybePickShatteredBiome(i, j, weirdness, middleBiome);
//                ResourceKey<Biome> shatteredCoastBiome        = this.pickShatteredCoastBiome(i, j, weirdness);
//                ResourceKey<Biome> slopeBiome              = this.pickSlopeBiome(i, j, weirdness);
//                ResourceKey<Biome> swampBiome              = this.pickSwampBiome(i, j, weirdness);
//
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, slopeBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBadlandsOrSlopeBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.farInlandContinentalness, this.erosions[1], weirdness, 0.0F, i == 0 ? slopeBiome : plateauBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.nearInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.midInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.farInlandContinentalness, this.erosions[2], weirdness, 0.0F, plateauBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiome);
//
//                if (weirdness.max() < 0L)
//                {
//                    this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, this.erosions[4], weirdness, 0.0F, beachBiome);
//                    this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
//                }
//                else
//                {
//                    this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
//                }
//
//                this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, shatteredCoastBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.nearInlandContinentalness, this.erosions[5], weirdness, 0.0F, shatteredBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, extremeHillsBiome);
//                if (weirdness.max() < 0L)
//                {
//                    this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, beachBiome);
//                }
//                else
//                {
//                    this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, middleBiome);
//                }
//
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, swampBiome);
//            }
//        }
//    }

//    protected void addLowSlice(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter weirdness) {
////        this.addSurfaceBiome(mapper, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_STONY_SHORE);
//
//        for (int i = 0; i < this.temperatures.length; ++i)
//        {
//            Climate.Parameter temperature = this.temperatures[i];
//
//            for (int j = 0; j < this.humidities.length; ++j)
//            {
//                Climate.Parameter humidity = this.humidities[j];
//
//                ResourceKey<Biome> middleBiome                  = this.pickMiddleBiome(i, j, weirdness);
//                ResourceKey<Biome> middleBadlandsOrSlopeBiome       = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
//
//                ResourceKey<Biome> beachBiome                   = this.pickBeachBiome(i, j);
//                ResourceKey<Biome> shatteredBiome               = this.maybePickShatteredBiome(i, j, weirdness, middleBiome);
//                ResourceKey<Biome> shatteredCoastBiome          = this.pickShatteredCoastBiome(i, j, weirdness);
//
//                ResourceKey<Biome> swampBiome                = this.pickSwampBiome(i, j, weirdness);
//
//                // Lowest to low erosion
//                this.addSurfaceBiome(mapper, temperature, humidity, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBadlandsOrSlopeBiome);
//
//                // Reduced to moderate erosion
//                this.addSurfaceBiome(mapper, temperature, humidity, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
//
//                // Moderate to increased erosion
//                this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, Climate.Parameter.span(this.erosions[3], this.erosions[4]), weirdness, 0.0F, beachBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
//
//                // High erosion
//                this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, shatteredCoastBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, this.nearInlandContinentalness, this.erosions[5], weirdness, 0.0F, shatteredBiome);
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, middleBiome);
//
//                // Highest erosion
//                this.addSurfaceBiome(mapper, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, beachBiome);
//
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, swampBiome);
//            }
//        }
//    }

//    protected void addValleys(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter weirdness) {
////        this.addSurfaceBiome(mapper, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, weirdness.max() < 0L ? AlfheimrBiomes.ALFHEIMR_STONY_SHORE : AlfheimrBiomes.ALFHEIMR_FROZEN_RIVER);
////        this.addSurfaceBiome(mapper, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, weirdness.max() < 0L ? AlfheimrBiomes.ALFHEIMR_STONY_SHORE : AlfheimrBiomes.ALFHEIMR_RIVER);
////        this.addSurfaceBiome(mapper, this.FROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_FROZEN_RIVER);
////        this.addSurfaceBiome(mapper, this.UNFROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_RIVER);
////        this.addSurfaceBiome(mapper, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_FROZEN_RIVER);
////        this.addSurfaceBiome(mapper, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_RIVER);
//
//        // Coastal watery valleys
////        this.addSurfaceBiome(mapper, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_FROZEN_RIVER);
////        this.addSurfaceBiome(mapper, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, AlfheimrBiomes.ALFHEIMR_RIVER);
//
//        // Inland watery valleys
//
//        //Disabled so the Frozen River doesn't cut into the Muskeg/Hot Springs
//        //this.addSurfaceBiome(mapper, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
//
//        for (int i = 0; i < this.temperatures.length; ++i)
//        {
//            Climate.Parameter temperature = this.temperatures[i];
//
//            for (int j = 0; j < this.humidities.length; ++j)
//            {
//                Climate.Parameter humidity = this.humidities[j];
//                ResourceKey<Biome> middleBiome               = this.pickMiddleBiome(i, j, weirdness);
//                ResourceKey<Biome> swampBiome                = this.pickSwampBiome(i, j, weirdness);
//
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
//
//                this.addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, swampBiome);
//            }
//        }
//    }

    public void addUndergroundBiomes(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper) {
//        this.addUndergroundBiome(mapper, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, AlfheimrBiomes.ALFHEIMR_CRYSTAL_CAVERN);
//        this.addUndergroundBiome(mapper, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, AlfheimrBiomes.ALFHEIMR_OVERGROWN_CAVERN);
//        this.addBottomBiome(mapper, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.erosions[0], this.erosions[1]), this.FULL_RANGE, 0.0F, AlfheimrBiomes.ALFHEIMR_CORRUPTED_CAVERN);
    }

//    protected ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
//    {
//        return temperatureIndex == 0 ? this.pickSlopeBiome(temperatureIndex, humidityIndex, weirdness) : this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
//    }
//
//    protected ResourceKey<Biome> pickSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
//    {
//        ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
//
//        if (temperatureIndex == 1) return biomeOrFallback(plateauBiome);
//        else if (temperatureIndex == 2) return biomeOrFallback(plateauBiome);
//        else if (temperatureIndex == 3 && humidityIndex >= 3) return biomeOrFallback(plateauBiome);
//        else return biomeOrFallback(plateauBiome);
//    }

//    protected ResourceKey<Biome> pickPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
//    {
//        if (weirdness.max() < 0l) return biomeOrFallback(this.PLATEAU_BIOMES[temperatureIndex][humidityIndex]);
//        else return biomeOrFallback(this.PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex], this.PLATEAU_BIOMES[temperatureIndex][humidityIndex]);
//    }
//
//    protected ResourceKey<Biome> pickExtremeHillsBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
//    {
//        ResourceKey<Biome> resourceKey = this.EXTREME_HILLS[temperatureIndex][humidityIndex];
//        return resourceKey == null ? this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : resourceKey;
//    }
//
//    protected ResourceKey<Biome> pickBeachBiome(int temperatureIndex, int humidityIndex)
//    {
//        if (temperatureIndex == 0)
//            return AlfheimrBiomes.ALFHEIMR_FROZEN_BEACH;
//        else if (temperatureIndex == 2 && humidityIndex < 2)
//        {
//            return biomeOrFallback(AlfheimrBiomes.ALFHEIMR_BEACH);
//        }
//        else
//        {
//            return temperatureIndex == 4 ? AlfheimrBiomes.ALFHEIMR_DESERT : AlfheimrBiomes.ALFHEIMR_BEACH;
//        }
//    }
//
//    protected ResourceKey<Biome> pickSwampBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
//    {
//        return biomeOrFallback(AlfheimrBiomes.ALFHEIMR_SWAMP, this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdness), AlfheimrBiomes.ALFHEIMR_LOST_SWAMP);
//    }

//    protected ResourceKey<Biome> maybePickShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> extremeHillsBiome)
//    {
//        return temperatureIndex > 1 && humidityIndex < 4 && weirdness.max() >= 0L ? biomeOrFallback(AlfheimrBiomes.ALFHEIMR_SAVANNA) : extremeHillsBiome;
//    }
//
//    protected ResourceKey<Biome> pickShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
//    {
//        ResourceKey<Biome> resourcekey = weirdness.max() >= 0L ? this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : this.pickBeachBiome(temperatureIndex, humidityIndex);
//        return this.maybePickShatteredBiome(temperatureIndex, humidityIndex, weirdness, resourcekey);
//    }

//    protected ResourceKey<Biome> pickPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
//        if (temperatureIndex <= 2) {
//            return weirdness.max() < 0L ? AlfheimrBiomes.ALFHEIMR_SKY_PEAKS : AlfheimrBiomes.ALFHEIMR_FROZEN_PEAKS;
//        } else {
//            return temperatureIndex == 3 ? AlfheimrBiomes.ALFHEIMR_STONY_PEAKS : this.pickBadlandsBiome(humidityIndex, weirdness);
//        }
//    }

//    protected ResourceKey<Biome> pickBadlandsBiome(int humidityIndex, Climate.Parameter weirdness)
//    {
//        if (humidityIndex < 2)
//        {
//            return weirdness.max() < 0L ? AlfheimrBiomes.ALFHEIMR_CORRUPTED_BADLANDS : AlfheimrBiomes.ALFHEIMR_BADLANDS;
//        }
//        else
//        {
//            return humidityIndex < 3 ? AlfheimrBiomes.ALFHEIMR_BADLANDS : AlfheimrBiomes.ALFHEIMR_SAVANNA;
//        }
//    }

//    protected ResourceKey<Biome> pickMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
//    {
//        if (weirdness.max() < 0L)
//        {
//            return this.MIDDLE_BIOMES[temperatureIndex][humidityIndex];
//        }
//        else
//        {
//            ResourceKey<Biome> variantBiome = this.MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex];
//            return variantBiome == null ? this.MIDDLE_BIOMES[temperatureIndex][humidityIndex] : variantBiome;
//        }
//    }

//    protected ResourceKey<Biome> pickIslandBiome(int temperatureIndex, int humidityIndex)
//    {
//        return biomeOrFallback(this.ISLAND_BIOMES[temperatureIndex][humidityIndex], AlfheimrBiomes.ALFHEIMR_FAIRY_RINGS, AlfheimrBiomes.ALFHEIMR_FAIRY_HILLS);
//    }

    public void addSurfaceBiome(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        mapper.accept(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0f), weirdness, offset), biome);
        mapper.accept(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0f), weirdness, offset), biome);
    }

    protected void addUndergroundBiome(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome)
    {
        mapper.accept(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.span(0.2F, 0.9F), weirdness, offset), biome);
    }

    private void addBottomBiome(BiConsumer<Climate.ParameterPoint, ResourceKey<Biome>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome)
    {
        mapper.accept(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.1F), weirdness, offset), biome);
    }

    @SafeVarargs
    private static ResourceKey<Biome> biomeOrFallback(ResourceKey<Biome>... biomes) {
        for (ResourceKey<Biome> key : biomes) {
            if (key == null)
                continue;

            return key;
        }
        return null;
    }
}
