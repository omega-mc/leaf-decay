package com.github.draylar.leafDecay.mixin;

import com.github.draylar.leafDecay.util.LeavesBreaker;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LeavesBlock.class)
public class LeafDecayMixin
{
	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;clearBlockState(Lnet/minecraft/util/math/BlockPos;Z)Z"), method = "onRandomTick")
	private void init(BlockState blockState_1, World world_1, BlockPos blockPos_1, Random random_1, CallbackInfo ci)
	{
		LeavesBreaker.onBreak(world_1, blockPos_1);
	}
}
