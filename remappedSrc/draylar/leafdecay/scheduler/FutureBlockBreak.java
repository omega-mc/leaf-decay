package draylar.leafdecay.scheduler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Provides information about a block that should break in the future.
 *
 * <p>This class stores information on the {@link World} the break should occur in,
 * the {@link BlockPos} it should occur at, as well as a current & final time counter.
 */
public class FutureBlockBreak {

    private final ServerWorld world;
    private final BlockPos pos;
    private final int maxTime;
    private int elapsedTime;

    /**
     * Primary constructor for {@link FutureBlockBreak}.
     *
     * @param world  world that this future break should occur in
     * @param pos  position that this break should occur at
     * @param maxTime  the time, in ticks, that the break waits through before occurring
     */
    public FutureBlockBreak(ServerWorld world, BlockPos pos, int maxTime) {
        this.world = world;
        this.pos = pos;
        this.maxTime = maxTime;
    }


    /**
     * Causes this {@link FutureBlockBreak} to break the block at its stored position.
     *
     * <p>The block broken will drop stacks as normal.
     */
    public void realize() {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        if (!world.isClient) {
            List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, null);
            DefaultedList<ItemStack> defaultedDropList = DefaultedList.ofSize(drops.size(), ItemStack.EMPTY);

            for (int i = 0; i < drops.size(); i++) {
                defaultedDropList.set(i, drops.get(i));
            }

            ItemScatterer.spawn(world, pos, defaultedDropList);
        }
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ServerWorld getWorld() {
        return world;
    }
}
