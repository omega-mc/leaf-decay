package com.github.draylar.leafDecay.util;

import com.github.draylar.leafDecay.scheduler.FutureLeafBreak;
import com.github.draylar.leafDecay.scheduler.LeafBreakHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class LeavesBreaker
{
    public static void onBreak(World world, BlockPos pos)
    {
        // check all nearby leaf blocks
        for(Direction direction : Direction.values())
        {
            BlockPos offset = pos.offset(direction, 1);
            BlockState newState = world.getBlockState(offset);

            if(newState.getBlock() instanceof LeavesBlock)
            {
                // if the leaf should decay, add it to the break list
                if(!newState.get(LeavesBlock.PERSISTENT) && newState.get(LeavesBlock.DISTANCE) == 7)
                {
                    LeafBreakHandler.addFutureBreak(new FutureLeafBreak(
                            world,
                            offset,
                            5
                    ));
                }
            }
        }
    }
}
