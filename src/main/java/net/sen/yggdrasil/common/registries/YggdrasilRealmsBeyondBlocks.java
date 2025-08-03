package net.sen.yggdrasil.common.registries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.yggdrasil.common.block.ModBushBlock;
import net.sen.yggdrasil.common.block.ModCropBlock;
import net.sen.yggdrasil.common.block.portal.alfheimr.AlfheimrPortalBlock;
import net.sen.yggdrasil.common.utils.ModUtils;

import java.util.Locale;
import java.util.function.Supplier;

public class YggdrasilRealmsBeyondBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModUtils.getModId());
    private static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(ModUtils.getModId());

    //Alfheimr
    public static final Supplier<Block> ALFHEIMR_PORTAL_BLOCK = createBlock("alfheimr_portal_block", AlfheimrPortalBlock::new);
    public static final Supplier<Block> ALFHEIMR_PORTAL_FRAME_BLOCK = createBlock("alfheimr_portal_frame_block");

    //Nidavellir

    //Atlantis

    //Skyopia

    private static Supplier<Block> createBlock(String name) {
        Supplier<Block> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }

    private static Supplier<Block> createBlock(String name, BlockBehaviour.Properties properties) {
        Supplier<Block> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new Block(properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }

    private static Supplier<Block> createBlock(String name, RotatedPillarBlock block) {
        return createBlock(name, block);
    }

    private static Supplier<Block> createShortGrass(String name) {
        return createBlock(name, () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));
    }

    private static Supplier<Block> createTallGrass(String name) {
        return createBlock(name, () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)));
    }

    private static Supplier<Block> createCrop(String name, ItemLike baseSeedId, VoxelShape[] shapeByAge) {
        return createBlock(name, () -> new ModCropBlock(baseSeedId, shapeByAge));
    }

    private static Supplier<Block> createBush(String name, ItemLike baseSeedId) {
        return createBlock(name, () -> new ModBushBlock(baseSeedId));
    }

    private static Supplier<SaplingBlock> createSapling(String name, TreeGrower treeGrower) {
        Supplier<SaplingBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new SaplingBlock(treeGrower, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }

    private static Supplier<LeavesBlock> createLeaves(String name) {
        Supplier<LeavesBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new LeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(YggdrasilRealmsBeyondBlocks::ocelotOrParrot).isSuffocating(YggdrasilRealmsBeyondBlocks::never).isViewBlocking(YggdrasilRealmsBeyondBlocks::never).ignitedByLava().isRedstoneConductor(YggdrasilRealmsBeyondBlocks::never)));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;

    }

    private static Supplier<Block> createRotatedPillarBlock(String name, BlockBehaviour.Properties properties) {
        Supplier<Block> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new RotatedPillarBlock(properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<StairBlock> createStairBlock(String name, Supplier<Block> baseBlock, BlockBehaviour.Properties properties) {
        Supplier<StairBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new StairBlock(baseBlock.get().defaultBlockState(), properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<SlabBlock> createSlabBlock(String name, BlockBehaviour.Properties properties) {
        Supplier<SlabBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new SlabBlock(properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<FenceBlock> createFenceBlock(String name, BlockBehaviour.Properties properties) {
        Supplier<FenceBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new FenceBlock(properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<FenceGateBlock> createFenceGateBlock(String name, WoodType woodType, BlockBehaviour.Properties properties) {
        Supplier<FenceGateBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new FenceGateBlock(woodType, properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<DoorBlock> createDoorBlock(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        Supplier<DoorBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new DoorBlock(blockSetType, properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<TrapDoorBlock> createTrapDoorBlock(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        Supplier<TrapDoorBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new TrapDoorBlock(blockSetType, properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<PressurePlateBlock> createPressurePlateBlock(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        Supplier<PressurePlateBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new PressurePlateBlock(blockSetType, properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
    private static Supplier<ButtonBlock> createButtonBlock(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        Supplier<ButtonBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new ButtonBlock(blockSetType, 30, properties));
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }
//    private static Supplier<StandingSignBlock> createSignBlock(String name, WoodType woodType, BlockBehaviour.Properties properties) {
//        Supplier<StandingSignBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new ModStandingSignBlock(woodType, properties));
//        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
//        return toReturn;
//    }
//    private static Supplier<WallSignBlock> createWallSignBlock(String name, WoodType woodType, BlockBehaviour.Properties properties) {
//        Supplier<WallSignBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new ModWallSignBlock(woodType, properties));
//        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
//        return toReturn;
//    }
//    private static Supplier<CeilingHangingSignBlock> createHangingSignBlock(String name, WoodType woodType, BlockBehaviour.Properties properties) {
//        Supplier<CeilingHangingSignBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new ModCeilingHangingSignBlock(woodType, properties));
//        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
//        return toReturn;
//    }
//    private static Supplier<WallHangingSignBlock> createWallHangingSignBlock(String name, WoodType woodType, BlockBehaviour.Properties properties) {
//        Supplier<WallHangingSignBlock> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), () -> new ModWallHangingSignBlock(woodType, properties));
//        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
//        return toReturn;
//    }

        private static <T extends Block> Supplier<T> createBlock(String name, Supplier<T> block) {
            Supplier<T> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), block);
            createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
            return toReturn;
        }

        private static <T extends Block> Supplier<Item> createBlockItem(String name, Supplier<T> block) {
            return BLOCK_ITEMS.register(name.toLowerCase(Locale.ROOT), () -> new BlockItem(block.get(), new Item.Properties()));
        }

    private static Boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return (boolean)true;
    }

    private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return (boolean)false;
    }

    private static Boolean ocelotOrParrot(BlockState p_50822_, BlockGetter p_50823_, BlockPos p_50824_, EntityType<?> p_50825_) {
        return (boolean)(p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}
