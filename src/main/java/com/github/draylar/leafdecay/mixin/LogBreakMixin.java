package com.github.draylar.leafdecay.mixin;

import com.github.draylar.leafdecay.scheduler.FutureLeafBreak;
import com.github.draylar.leafdecay.scheduler.LeafBreakHandler;
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

@Mixin(Block.class)
public class LogBreakMixin {
    @Inject(at = @At("TAIL"), method = "onBreak")

    private void afterBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info) {
        if (state.getBlock() instanceof LogBlock) {
            BlockPos upPosition = pos.up();
            BlockState upState = world.getBlockState(upPosition);

            if (upState.getBlock() instanceof LeavesBlock) {
                FutureLeafBreak futureLeafBreak = new FutureLeafBreak(world, upPosition, 4);
                LeafBreakHandler.addFutureBreak(futureLeafBreak);
            }
        }
    }
}