package com.liuyue.igny.mixins.rule.wetSpongeCanAbsorbLava;

import com.liuyue.igny.IGNYSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
//#if MC>=12102
//$$ import net.minecraft.world.level.redstone.Orientation;
//#endif

@Mixin(WetSpongeBlock.class)
public abstract class WetSpongeBlockMixin extends Block{
    public WetSpongeBlockMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block,
                                //#if MC >= 12102
                                //$$ Orientation wireOrientation
                                //#else
                                BlockPos blockPos2
                                //#endif
            , boolean bl) {
        if (removeFluidBreadthFirstSearch(level, blockPos)) {
            level.setBlock(blockPos, Blocks.SPONGE.defaultBlockState(), 2);
            //#if MC > 12001
            level.playSound(null, blockPos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1.0F, 1.0F);
            //#else
            //$$ level.levelEvent(2001, blockPos, Block.getId(Blocks.WATER.defaultBlockState()));
            //#endif
        }
    }

    @Unique
    private boolean removeFluidBreadthFirstSearch(Level level, BlockPos blockPos) {
        return BlockPos.breadthFirstTraversal(
                blockPos,
                6,
                65,
                (blockPosx, consumer) -> {
                    for (Direction direction : ALL_DIRECTIONS) {
                        consumer.accept(blockPosx.relative(direction));
                    }
                },
                blockPos2 -> {
                    if (blockPos2.equals(blockPos)) {
                        //#if MC >= 12104
                        //$$ return BlockPos.TraversalNodeStatus.ACCEPT;
                        //#else
                        return true;
                        //#endif
                    } else {
                        BlockState blockState = level.getBlockState(blockPos2);
                        FluidState fluidState = level.getFluidState(blockPos2);
                        if (!shouldAbsorb(fluidState)) {
                            //#if MC >= 12104
                            //$$ return BlockPos.TraversalNodeStatus.SKIP;
                            //#else
                            return false;
                            //#endif
                        } else if (blockState.getBlock() instanceof BucketPickup bucketPickup
                                && !bucketPickup.pickupBlock(
                                        //#if MC > 12001
                                        null,
                                       //#endif
                                level, blockPos2, blockState).isEmpty()) {
                            //#if MC >= 12104
                            //$$ return BlockPos.TraversalNodeStatus.ACCEPT;
                            //#else
                            return true;
                            //#endif
                        } else {
                            if (blockState.getBlock() instanceof LiquidBlock) {
                                level.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 3);
                            } else {
                                if (!blockState.is(Blocks.KELP)
                                        && !blockState.is(Blocks.KELP_PLANT)
                                        && !blockState.is(Blocks.SEAGRASS)
                                        && !blockState.is(Blocks.TALL_SEAGRASS)) {
                                    //#if MC >= 12104
                                    //$$ return BlockPos.TraversalNodeStatus.SKIP;
                                    //#else
                                    return false;
                                    //#endif
                                }

                                BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(blockPos2) : null;
                                dropResources(blockState, level, blockPos2, blockEntity);
                                level.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 3);
                            }
                            //#if MC >= 12104
                            //$$ return BlockPos.TraversalNodeStatus.ACCEPT;
                            //#else
                            return true;
                            //#endif
                        }
                    }
                }
        ) > 1;
    }

    @Unique
    private boolean shouldAbsorb(FluidState fluidState) {
        return fluidState.is(FluidTags.LAVA) && IGNYSettings.wetSpongeCanAbsorbLava;
    }
}
