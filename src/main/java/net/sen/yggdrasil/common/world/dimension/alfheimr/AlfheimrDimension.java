package net.sen.yggdrasil.common.world.dimension.alfheimr;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.sen.yggdrasil.common.utils.ModUtils;
import net.sen.yggdrasil.common.world.biomes.alfheimr.AlfheimrBiomes;

import java.util.List;
import java.util.OptionalLong;

public class AlfheimrDimension {
    private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0);
    private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();

    private static final ResourceKey<DensityFunction> Y = vanillaKey("y");
    private static final ResourceKey<DensityFunction> SHIFT_X = vanillaKey("shift_x");
    private static final ResourceKey<DensityFunction> SHIFT_Z = vanillaKey("shift_z");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = vanillaKey("overworld/base_3d_noise");
    private static final ResourceKey<NormalNoise.NoiseParameters> BASE_3D_NOISE_END = vanillaKeyNoise("end/base_3d_noise");
    private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = vanillaKey("overworld/caves/spaghetti_roughness_function");
    private static final ResourceKey<DensityFunction> ENTRANCES = vanillaKey("overworld/caves/entrances");
    private static final ResourceKey<DensityFunction> NOODLE = vanillaKey("overworld/caves/noodle");
    private static final ResourceKey<DensityFunction> PILLARS = vanillaKey("overworld/caves/pillars");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D = vanillaKey("overworld/caves/spaghetti_2d");

    public static final ResourceKey<Level> ALFHEIMR_LEVEL = ResourceKey.create(Registries.DIMENSION, name("alfheimr"));
    public static final ResourceKey<NoiseGeneratorSettings> ALFHEIMR_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, name("alfheimr"));
    public static final ResourceKey<DimensionType> ALFHEIMR_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, name("alfheimr"));
    public static final ResourceKey<LevelStem> ALFHEIMR_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, name("alfheimr"));
    public static final ResourceLocation ALFHEIMR_ID = ModUtils.getModPath("alfheimr");

    public static final int MAX_HEIGHT = 384;
    public static final int MIN_HEIGHT = -64;
    public static final int SEA_LEVEL = 128;

    public static NoiseGeneratorSettings bootstrapNoise(BootstrapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParameters = context.lookup(Registries.NOISE);

        return new NoiseGeneratorSettings(
                NoiseSettings.create(0, 256, 2, 1),
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                alfheimrNoiseRouter(densityFunctions, noiseParameters),
                alfheimrSurfaceRules(),
                List.of(),
                -64,
                false,
                false,
                false,
                true
        );
    }

    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        context.register(ALFHEIMR_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                0.6, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                MIN_HEIGHT, // minY
                MAX_HEIGHT, // height
                MAX_HEIGHT, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)));
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator  = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(AlfheimrBiomes.TEST_BIOME)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD)
        );

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ALFHEIMR_DIM_TYPE), wrappedChunkGenerator);

        context.register(ALFHEIMR_LEVEL_STEM, stem);
    }

    private static NoiseRouter alfheimrNoiseRouter(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        DensityFunction aquiferBarrier = DensityFunctions.zero();
        DensityFunction aquiferFluidLevelFloodedness = DensityFunctions.zero();
        DensityFunction aquiferFluidLevelSpread = DensityFunctions.zero();
        DensityFunction aquiferLava = DensityFunctions.zero();
        DensityFunction shiftX = getFunction(densityFunctions, SHIFT_X);
        DensityFunction shiftZ = getFunction(densityFunctions, SHIFT_Z);
        DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction continents = DensityFunctions.zero();
        DensityFunction erosion = DensityFunctions.zero();
        DensityFunction depth = DensityFunctions.zero();
        DensityFunction factor = getFunction(densityFunctions, NoiseRouterData.FACTOR);
        DensityFunction ridges = getFunction(densityFunctions, NoiseRouterData.RIDGES);
        DensityFunction initialDensityWithoutJaggedness = noiseGradientDensity(DensityFunctions.cache2d(factor), depth);
        DensityFunction finalDensity = DensityFunctions.mul(
                DensityFunctions.constant(0.64d),
                DensityFunctions.interpolated(DensityFunctions.blendDensity(DensityFunctions.add(
                        DensityFunctions.constant(-0.234375),
                        DensityFunctions.mul(
                                DensityFunctions.yClampedGradient(
                                        0,
                                        4,
                                        1.0,
                                        32
                                ),
                                DensityFunctions.add(
                                        DensityFunctions.constant(0.234375),
                                        DensityFunctions.add(
                                                DensityFunctions.constant(-23.4375),
                                                DensityFunctions.mul(
                                                        DensityFunctions.yClampedGradient(
                                                                1,
                                                                184,
                                                                0.0,
                                                                440
                                                        ),
                                                        DensityFunctions.add(
                                                                DensityFunctions.constant(23.4375),
                                                                DensityFunctions.noise(noiseParameters.getOrThrow(BASE_3D_NOISE_END))
                                                        )
                                                )
                                        )
                                )
                        )
                )))
        ).squeeze();
        DensityFunction veinToggle = DensityFunctions.zero();
        DensityFunction veinRidged = DensityFunctions.zero();
        DensityFunction veinGap = DensityFunctions.zero();

        return new NoiseRouter(
                aquiferBarrier,
                aquiferFluidLevelFloodedness,
                aquiferFluidLevelSpread,
                aquiferLava,
                temperature,
                vegetation,
                continents,
                erosion,
                depth,
                ridges,
                initialDensityWithoutJaggedness,
                finalDensity,
                veinToggle,
                veinRidged,
                veinGap
        );
    }

    private static SurfaceRules.RuleSource alfheimrSurfaceRules() {
        return SurfaceRules.sequence(
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(
                                SurfaceRules.ifTrue(
                                        SurfaceRules.isBiome(Biomes.WOODED_BADLANDS),
                                        SurfaceRules.ifTrue(
                                                SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2),
                                                SurfaceRules.sequence(
                                                        SurfaceRules.ifTrue(
                                                                SurfaceRules.noiseCondition(Noises.SURFACE, -0.5454d, -0.909d),
                                                                SurfaceRules.state(Blocks.COARSE_DIRT.defaultBlockState())
                                                        ),
                                                        SurfaceRules.ifTrue(
                                                                SurfaceRules.noiseCondition(Noises.SURFACE, 0.1818d, -0.1818d),
                                                                SurfaceRules.state(Blocks.COARSE_DIRT.defaultBlockState())
                                                        ),
                                                        SurfaceRules.ifTrue(
                                                                SurfaceRules.noiseCondition(Noises.SURFACE, 0.909d, 0.5454d),
                                                                SurfaceRules.state(Blocks.COARSE_DIRT.defaultBlockState())
                                                        ),
                                                        SurfaceRules.sequence(
                                                                SurfaceRules.ifTrue(
                                                                        SurfaceRules.waterStartCheck(0, 0),
                                                                        SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())
                                                                ),
                                                                SurfaceRules.state(Blocks.DIRT.defaultBlockState())
                                                        )
                                                )
                                        )
                                )
                        ))
                )
        );
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> registry, ResourceKey<DensityFunction> key) {
        return wrap(registry.getOrThrow(key));
    }

    private static DensityFunction wrap(Holder.Reference<DensityFunction> holder) {
        return new DensityFunctions.HolderHolder(holder);
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction p_209472_, DensityFunction p_209473_, int p_209474_, int p_209475_, int p_209476_) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(p_209472_, p_209474_, p_209475_ + 1, p_209473_, DensityFunctions.constant(p_209476_)));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction densityfunction = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0), densityfunction.quarterNegative());
    }

    private static ResourceKey<NoiseGeneratorSettings> createKey(final String id) {
        return ResourceKey.create(Registries.NOISE_SETTINGS, name(id));
    }

    private static ResourceLocation name(String name) {
        return ResourceLocation.fromNamespaceAndPath(ModUtils.getModId(), name);
    }

    private static ResourceKey<DensityFunction> vanillaKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, ModUtils.getMinecraftPath("overworld/" + name));
    }

    private static ResourceKey<NormalNoise.NoiseParameters> vanillaKeyNoise(String name) {
        return ResourceKey.create(Registries.NOISE, ModUtils.getMinecraftPath(name));
    }
}
