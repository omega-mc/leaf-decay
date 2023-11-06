package draylar.leafdecay.scheduler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeafBreakHandler {
    private static final ArrayList<FutureBlockBreak> BREAK_LIST = new ArrayList<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(tick -> {
            List<FutureBlockBreak> nearbyFutureBreakOrigins = new ArrayList<>();

            for (Iterator<FutureBlockBreak> iterator = BREAK_LIST.iterator(); iterator.hasNext();) {
                FutureBlockBreak leafBreak = iterator.next();
                int elapsed = leafBreak.getElapsedTime();

                if (elapsed >= leafBreak.getMaxTime()) {
                    // should be broken
                    leafBreak.realize();
                    iterator.remove();
                    nearbyFutureBreakOrigins.add(leafBreak);
                } else {
                    // increment time
                    leafBreak.setElapsedTime(elapsed + 1);
                }
            }

            // add all new neighbor breaks to the breakList after we iterate over it to avoid CME
            nearbyFutureBreakOrigins.forEach(leafBreak -> addNearbyFutureBreaks(leafBreak.getWorld(), leafBreak.getPos()));
        });
    }

    public static void addNearbyFutureBreaks(ServerWorld world, BlockPos pos) {
        // check all nearby leaf blocks
        for (Direction direction : Direction.values()) {
            BlockPos offset = new BlockPos(pos.offset(direction, 1));
            BlockState newState = world.getBlockState(offset);

            // ensure new position is a leaf and that the break list doesn't already contain it
            if (!breakListContains(offset) &&
                    world.getBlockState(offset).getBlock() instanceof LeavesBlock leavesBlock) {

                // if the leaf would naturally decay, add it to the break list
                // (hasRandomTicks() is identical to shouldDecay() but is public without an access widener)
                if (leavesBlock.hasRandomTicks(newState)) {
                    LeafBreakHandler.addFutureBreak(new FutureBlockBreak(world, offset, 5));
                }
            }
        }
    }

    private static boolean breakListContains(BlockPos offset) {
        for (FutureBlockBreak futureBreak : BREAK_LIST) {
            if (futureBreak.getPos().equals(offset)) {
                return true;
            }
        }

        return false;
    }


    public static void addFutureBreak(FutureBlockBreak futureBreak) {
        BREAK_LIST.add(futureBreak);
    }
}
