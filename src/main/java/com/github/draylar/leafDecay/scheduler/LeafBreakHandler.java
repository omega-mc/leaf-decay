package com.github.draylar.leafDecay.scheduler;

import com.github.draylar.leafDecay.LeafDecayConfig;
import com.github.draylar.leafDecay.util.LeavesBreaker;
import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class LeafBreakHandler
{
    private static ArrayList<FutureLeafBreak> breakList;

    static
    {
        breakList = new ArrayList<>();

        ServerTickCallback.EVENT.register(tick ->
        {
            for (int i = 0; i < breakList.size(); i++)
            {
                FutureLeafBreak leafBreak = breakList.get(i);
                if (leafBreak.getElapsedTime() >= leafBreak.getMaxTime())
                {
                    breakLeafBlock(leafBreak);
                    breakList.remove(leafBreak);
                    LeavesBreaker.onBreak(leafBreak.getWorld(), leafBreak.getPos());
                } else leafBreak.setElapsedTime(leafBreak.getElapsedTime() + 1);
            }
        });
    }

    public static void init()
    {

    }

    public static void addFutureBreak(FutureLeafBreak futureBreak)
    {
        breakList.add(futureBreak);
    }

    private static void breakLeafBlock(FutureLeafBreak futureBreak)
    {
        BlockPos pos = futureBreak.getPos();
        World world = futureBreak.getWorld();
        BlockState blockState_1 = world.getBlockState(pos);

        if (!blockState_1.isAir())
        {
            FluidState fluidState_1 = world.getFluidState(pos);

            // play sound && add particles
            if(AutoConfig.getConfigHolder(LeafDecayConfig.class).getConfig().playSound)
            {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1, 1, false);
            }

            if(AutoConfig.getConfigHolder(LeafDecayConfig.class).getConfig().leafParticles)
            {
                if(world.isClient)
                {
                    MinecraftClient.getInstance().particleManager.addBlockBreakParticles(pos, blockState_1);
                }
            }

            // drop stacks
            BlockEntity blockEntity_1 = blockState_1.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
            Block.dropStacks(blockState_1, world, pos, blockEntity_1);

            world.setBlockState(pos, fluidState_1.getBlockState(), 3);
        }
    }
}
