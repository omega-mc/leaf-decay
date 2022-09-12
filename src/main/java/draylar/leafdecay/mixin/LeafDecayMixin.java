package draylar.leafdecay.mixin;

import draylar.leafdecay.scheduler.LeafBreakHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.random.Random;

@Mixin(LeavesBlock.class)
public class LeafDecayMixin {

	@Inject(
			method = "randomTick",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/server/world/ServerWorld;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
			)
	)
	private void init(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		LeafBreakHandler.addNearbyFutureBreaks(world, pos);
	}
}
