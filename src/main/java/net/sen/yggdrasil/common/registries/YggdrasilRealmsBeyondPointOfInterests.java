package net.sen.yggdrasil.common.registries;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.yggdrasil.common.utils.ModUtils;

import java.util.function.Supplier;

public class YggdrasilRealmsBeyondPointOfInterests {
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, ModUtils.getModId());

    public static DeferredHolder<PoiType, PoiType> ALFHEIMR_PORTAL = POI.register("alfheimr_portal", () -> new PoiType(ImmutableSet.copyOf(YggdrasilRealmsBeyondBlocks.ALFHEIMR_PORTAL_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1));

    public static void register(IEventBus eventBus) {
        POI.register(eventBus);
    }
}
