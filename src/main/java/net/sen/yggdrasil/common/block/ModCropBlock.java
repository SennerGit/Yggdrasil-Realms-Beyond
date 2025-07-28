package net.sen.yggdrasil.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModCropBlock extends CropBlock {
    public static final MapCodec<ModCropBlock> CODEC = simpleCodec(ModCropBlock::new);
    private final VoxelShape[] SHAPE_BY_AGE;

    private final ItemLike baseSeedId;

    public ModCropBlock(Properties properties) {
        super(properties);
        this.SHAPE_BY_AGE  = new VoxelShape[]{
                Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
                Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
                Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
                Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
                Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
                Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
                Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
                Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0)
        }; // Default shape array for 8 growth stages
        this.baseSeedId = Items.WHEAT_SEEDS; // Default seed item, can be overridden in constructor
    }

    public ModCropBlock(ItemLike baseSeedId, VoxelShape[] SHAPE_BY_AGE) {
        super(Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY)
        );
        this.baseSeedId = baseSeedId;
        this.SHAPE_BY_AGE = SHAPE_BY_AGE;
    }

    @Override
    public MapCodec<ModCropBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.baseSeedId;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}
