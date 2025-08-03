package net.sen.yggdrasil.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.yggdrasil.YggdrasilRealmsBeyond;
import net.sen.yggdrasil.common.world.biomes.carvers.AlfheimrCaveWorldCarver;

public class YggdrasilRealmsBeyondCarvers {
    public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(Registries.CARVER, YggdrasilRealmsBeyond.MODID);

    public static final DeferredHolder<WorldCarver<?>, WorldCarver<CaveCarverConfiguration>> ALFHEIMR_CAVE = CARVERS.register("alfheimr_carver", () -> new AlfheimrCaveWorldCarver(CaveCarverConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        CARVERS.register(eventBus);
    }
}
