package draylar.leafdecay.mixin;

import draylar.leafdecay.scheduler.FutureBlockBreak;
import draylar.leafdecay.scheduler.LeafBreakHandler;
import net.minecraft.block.*;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class LogBreakMixin {

  @Inject(method = "onBreak", at = @At("TAIL"))
  private void afterBreak(World world, BlockPos pos, BlockState state,
                          PlayerEntity player, CallbackInfo info) {
    if (!world.isClient && (state.getBlock() instanceof PillarBlock) &&
        (state.isOf(Blocks.OAK_LOG) || state.isOf(Blocks.BIRCH_LOG) ||
         state.isOf(Blocks.ACACIA_LOG) || state.isOf(Blocks.SPRUCE_LOG) ||
         state.isOf(Blocks.JUNGLE_LOG) || state.isOf(Blocks.CHERRY_LOG) ||
         state.isOf(Blocks.DARK_OAK_LOG) || state.isOf(Blocks.MANGROVE_LOG))) {

      BlockPos upPosition = pos.up();
      BlockState upState = world.getBlockState(upPosition);

      // trigger chain break on the leaf block above a log
      if (upState.getBlock() instanceof LeavesBlock) {
        FutureBlockBreak futureLeafBreak =
            new FutureBlockBreak((ServerWorld)world, upPosition, 4);
        LeafBreakHandler.addFutureBreak(futureLeafBreak);
      }
    }
  }
}