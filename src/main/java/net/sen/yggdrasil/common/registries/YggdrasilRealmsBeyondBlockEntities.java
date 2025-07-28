package net.sen.yggdrasil.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.yggdrasil.common.utils.ModUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class YggdrasilRealmsBeyondBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModUtils.getModId());

//    public static final Supplier<BlockEntityType<ModSignBlockEntity>> SIGN = createBlockEntity("sign", ModSignBlockEntity::new,
//            SOMBER_STANDING_SIGN,
//            SOMBER_WALL_SIGN
//    );
//    public static final Supplier<BlockEntityType<ModHangingSignBlockEntity>> HANGING_SIGN = createBlockEntity("hanging_sign", ModHangingSignBlockEntity::new,
//            SOMBER_CEILING_HANGING_SIGN,
//            SOMBER_WALL_HANGING_SIGN
//    );

    @SuppressWarnings("DataFlowIssue")
    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> createBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<? extends Supplier<? extends Block>> blocks) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
    }
    @SafeVarargs
    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> createBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
        return createBlockEntity(name, supplier, List.of(blocks));
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
