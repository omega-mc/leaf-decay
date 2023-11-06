package draylar.leafdecay.scheduler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        if (world.isClient) {
            return;
        }

        BlockState state = world.getBlockState(pos);

        Block.dropStacks(state, world, pos);
        world.removeBlock(pos, false);
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
