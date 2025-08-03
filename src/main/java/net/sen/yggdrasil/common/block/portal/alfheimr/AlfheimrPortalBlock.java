package net.sen.yggdrasil.common.block.portal.alfheimr;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sen.yggdrasil.YggdrasilRealmsBeyond;
import net.sen.yggdrasil.common.world.dimension.alfheimr.AlfheimrDimension;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AlfheimrPortalBlock extends Block implements Portal {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public AlfheimrPortalBlock() {
        super(
                Properties.of()
                        .pushReaction(PushReaction.BLOCK)
                        .strength(-1.0F)
                        .noCollission()
                        .lightLevel((state) -> 10)
                        .sound(SoundType.GLASS)
                        .noLootTable()
        );
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.X ? X_AABB : Z_AABB;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction.Axis direction$axis = direction.getAxis();
        Direction.Axis direction$axis1 = state.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();

        return !flag && neighborState.getBlock() != this && !new AlfheimrPortalShape(level, pos, direction$axis1).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int i = 0; i < 4; ++i) {
            double x = (double) pos.getX() + random.nextDouble();
            double y = (double) pos.getY() + random.nextDouble();
            double z = (double) pos.getZ() + random.nextDouble();
            double xSpeed = ((double) random.nextFloat() - 0.5D) * 0.5D;
            double ySpeed = ((double) random.nextFloat() - 0.5D) * 0.5D;
            double zSpeed = ((double) random.nextFloat() - 0.5D) * 0.5D;
            int j = random.nextInt(2) * 2 - 1;

            if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                x = (double) pos.getX() + 0.5D + 0.25D * (double) j;
                xSpeed = random.nextFloat() * 2.0F * (float) j;
            } else {
                z = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
            }

            level.addParticle(ParticleTypes.PORTAL, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return ItemStack.EMPTY;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> {
                return switch (state.getValue(AXIS)) {
                    case Z -> state.setValue(AXIS, Direction.Axis.X);
                    case X -> state.setValue(AXIS, Direction.Axis.Z);
                    default -> state;
                };
            }
            default -> {
                return state;
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return this.getTransitionTime(level, entity);
    }

    private int getTransitionTime(Level level, Entity entity) {
        return entity instanceof Player player ? Math.max(1, level.getGameRules().getInt(player.getAbilities().invulnerable ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)) : 0;
    }

    @Override
    public @Nullable DimensionTransition getPortalDestination(ServerLevel serverLevel, Entity entity, BlockPos blockPos) {
        ResourceKey<Level> resourceKey = serverLevel.dimension() == AlfheimrDimension.ALFHEIMR_LEVEL ? Level.OVERWORLD : AlfheimrDimension.ALFHEIMR_LEVEL;
        ServerLevel destination = serverLevel.getServer().getLevel(resourceKey);
        if (destination == null) {
            return null;
        } else {
            boolean flag = destination.dimension() == AlfheimrDimension.ALFHEIMR_LEVEL;
            WorldBorder worldBorder = destination.getWorldBorder();
            double d0 = DimensionType.getTeleportationScale(serverLevel.dimensionType(), destination.dimensionType());
            BlockPos blockPos1 = worldBorder.clampToBounds(entity.getX() * d0, entity.getY(), entity.getZ() * d0);
            return this.getExitPortal(destination, entity, blockPos, blockPos1, flag, worldBorder);
        }
    }

    @Nullable
    private DimensionTransition getExitPortal(ServerLevel destination, Entity entity, BlockPos pos, BlockPos exitPos, boolean isInNewDimention, WorldBorder worldBorder) {
        Optional<BlockPos> potentialPortalSpot = AlfheimrPortalForcer.findClosestPortalPosition(destination, exitPos, isInNewDimention, worldBorder);
        BlockUtil.FoundRectangle rectangle;
        DimensionTransition.PostDimensionTransition post;
        if (potentialPortalSpot.isPresent()) {
            BlockPos blockPos = potentialPortalSpot.get();
            BlockState blockState = destination.getBlockState(blockPos);
            rectangle = BlockUtil.getLargestRectangleAround(blockPos, blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, posPredicate -> destination.getBlockState(posPredicate) == blockState);
            post = AlfheimrPortalForcer.PLAY_PORTAL_SOUND.then(traveler -> traveler.placePortalTicket(blockPos));
        } else {
            Direction.Axis axis = entity.level().getBlockState(pos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> createdPortal = AlfheimrPortalForcer.createPortal(destination, exitPos, axis);
            if (createdPortal.isPresent()) {
                YggdrasilRealmsBeyond.LOGGER.error("Unable to create a portal, likely target out of worldborder");
                return null;
            }

            rectangle = createdPortal.get();
            post = AlfheimrPortalForcer.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET);
        }

        return NetherPortalBlock.getDimensionTransitionFromExit(entity, pos, rectangle, destination, post);
    }
}