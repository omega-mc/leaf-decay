package com.github.draylar.leafdecay.mixin;

import com.github.draylar.leafdecay.util.LeavesBreaker;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LeavesBlock.class)
public class LeafDecayMixin {

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"), method = "randomTick")
	private void init(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		LeavesBreaker.onBreak(world, pos);
	}
}
