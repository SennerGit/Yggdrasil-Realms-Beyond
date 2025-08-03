package net.sen.yggdrasil.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.sen.yggdrasil.common.block.portal.alfheimr.AlfheimrPortalBlock;
import net.sen.yggdrasil.common.block.portal.alfheimr.AlfheimrPortalShape;
import net.sen.yggdrasil.common.registries.YggdrasilRealmsBeyondBlocks;

import java.util.Optional;
import java.util.function.Predicate;

public class PortalActivatorItem extends Item {
    private static int level;

    public PortalActivatorItem(int level) {
        super(new Properties()
                .stacksTo(1)
                .durability(switch(level){
                    case 0 -> 8;
                    case 1 -> 16;
                    case 2 -> 32;
                    default -> 8;
                })
        );

        this.level = level;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null) {
            if (context.getPlayer().level().dimension() == Level.OVERWORLD) {
                for (Direction direction : Direction.Plane.VERTICAL) {
                    BlockPos framePos = context.getClickedPos().relative(direction);
                    Optional<AlfheimrPortalShape> optionalAlfheimr = findAlfheimrPortalShape(context.getLevel(), framePos, shape -> shape.isValid() && shape.getPortalBlocks() == 0, Direction.Axis.X);
                    if (this.level >= 0) {
                        if (optionalAlfheimr.isPresent()) {
                            optionalAlfheimr.get().createPortalBlocks();
                            context.getLevel().playSound(context.getPlayer(), context.getClickedPos(), SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                            context.getItemInHand().hurtAndBreak(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
                            return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
                        }
                    }
                }
            }
        }


        return InteractionResult.FAIL;
    }

    public static Optional<AlfheimrPortalShape> findAlfheimrPortalShape(LevelAccessor accessor, BlockPos pos, Predicate<AlfheimrPortalShape> shape, Direction.Axis axis) {
        Optional<AlfheimrPortalShape> optional = Optional.of(new AlfheimrPortalShape(accessor, pos, axis)).filter(shape);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis oppositeAxis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new AlfheimrPortalShape(accessor, pos, oppositeAxis)).filter(shape);
        }
    }
}
