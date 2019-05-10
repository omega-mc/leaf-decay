package com.github.draylar.leafDecay.scheduler;

import com.github.draylar.leafDecay.util.LeavesBreaker;
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
        futureBreak.getWorld().breakBlock(futureBreak.getPos(), true);
    }
}
