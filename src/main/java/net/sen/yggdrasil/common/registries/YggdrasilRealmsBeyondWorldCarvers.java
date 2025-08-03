package net.sen.yggdrasil.common.registries;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.yggdrasil.YggdrasilRealmsBeyond;
import net.sen.yggdrasil.common.utils.ModUtils;
import net.sen.yggdrasil.common.world.biomes.carvers.AlfheimrCaveWorldCarver;

public class YggdrasilRealmsBeyondWorldCarvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> ALFHEIMR_CAVE =
            ResourceKey.create(Registries.CONFIGURED_CARVER,
                    ModUtils.getModPath("alfheimr_carver"));

    public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        context.register(ALFHEIMR_CAVE, YggdrasilRealmsBeyondCarvers.ALFHEIMR_CAVE.get().configured(new CaveCarverConfiguration(0.5F, UniformHeight.of(VerticalAnchor.aboveBottom(5), VerticalAnchor.belowTop(1)), ConstantFloat.of(0.5F), VerticalAnchor.aboveBottom(10), BuiltInRegistries.BLOCK.getOrCreateTag(YggdrasilRealmsBeyondTags.Blocks.CARVER_REPLACEABLES), ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), ConstantFloat.of(-0.7F))));
    }
}
