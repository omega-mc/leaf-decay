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

    private static final ArrayList<FutureBlockBreak> breakList = new ArrayList<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(tick -> {
            List<FutureBlockBreak> nearbyFutureBreakOrigins = new ArrayList<>();

            for(Iterator<FutureBlockBreak> iterator = breakList.iterator(); iterator.hasNext();) {
                FutureBlockBreak leafBreak = iterator.next();
                // should be broken
                if (leafBreak.getElapsedTime() >= leafBreak.getMaxTime()) {
                    leafBreak.realize();
                    iterator.remove();
                    nearbyFutureBreakOrigins.add(leafBreak);
                }

                // increment time
                else {
                    leafBreak.setElapsedTime(leafBreak.getElapsedTime() + 1);
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
            if (newState.getBlock() instanceof LeavesBlock && !breakListContains(offset)) {
                // if the leaf would naturally decay, add it to the break list
                if (!newState.get(LeavesBlock.PERSISTENT) && newState.get(LeavesBlock.DISTANCE) == 7) {
                    LeafBreakHandler.addFutureBreak(new FutureBlockBreak(world, offset, 5));
                }
            }
        }
    }

    private static boolean breakListContains(BlockPos offset) {
        for(FutureBlockBreak futureBreak : breakList) {
            if(futureBreak.getPos().equals(offset)) {
                return true;
            }
        }

        return false;
    }


    public static void addFutureBreak(FutureBlockBreak futureBreak) {
        breakList.add(futureBreak);
    }
}
