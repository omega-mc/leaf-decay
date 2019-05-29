package com.github.draylar.leafDecay.mixin;

import com.github.draylar.leafDecay.scheduler.FutureLeafBreak;
import com.github.draylar.leafDecay.scheduler.LeafBreakHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Helps kick-start leaf decay process by decaying the 4 leaves around a log that's broken if it's at the top of a tree.
 */
@Mixin(Block.class)
public class LogBreakMixin
{
    @Inject(at = @At("TAIL"), method = "onBreak")
    private void afterBreak(World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1, CallbackInfo ci)
    {
        if(blockState_1.getBlock() instanceof LogBlock)
        {
            BlockPos upPosition = blockPos_1.up();
            BlockState upState = world_1.getBlockState(upPosition);

            if(upState.getBlock() instanceof LeavesBlock)
            {
                FutureLeafBreak futureLeafBreak = new FutureLeafBreak(world_1, upPosition, 4);
                LeafBreakHandler.addFutureBreak(futureLeafBreak);
            }
        }
    }
}
