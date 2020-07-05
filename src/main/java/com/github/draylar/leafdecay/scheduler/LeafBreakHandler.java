package com.github.draylar.leafdecay.scheduler;

import com.github.draylar.leafdecay.util.LeavesBreaker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class LeafBreakHandler {

    private static ArrayList<FutureLeafBreak> breakList;

    static {
        breakList = new ArrayList<>();

        ServerTickEvents.END_SERVER_TICK.register(tick -> {
            for (int i = 0; i < breakList.size(); i++) {
                FutureLeafBreak leafBreak = breakList.get(i);
                if (leafBreak.getElapsedTime() >= leafBreak.getMaxTime()) {
                    breakLeafBlock(leafBreak);
                    breakList.remove(leafBreak);
                    LeavesBreaker.onBreak(leafBreak.getWorld(), leafBreak.getPos());
                } else leafBreak.setElapsedTime(leafBreak.getElapsedTime() + 1);
            }
        });
    }

    public static void init() {

    }

    public static void addFutureBreak(FutureLeafBreak futureBreak) {
        breakList.add(futureBreak);
    }

    private static void breakLeafBlock(FutureLeafBreak futureBreak) {
        BlockState state = futureBreak.getWorld().getBlockState(futureBreak.getPos());
        futureBreak.getWorld().setBlockState(futureBreak.getPos(), Blocks.AIR.getDefaultState());

        if (!futureBreak.getWorld().isClient) {
            List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) futureBreak.getWorld(), futureBreak.getPos(), null);
            DefaultedList<ItemStack> defaultedDropList = DefaultedList.ofSize(drops.size(), ItemStack.EMPTY);

            for (int i = 0; i < drops.size(); i++) {
                defaultedDropList.set(i, drops.get(i));
            }

            ItemScatterer.spawn(
                    futureBreak.getWorld(),
                    futureBreak.getPos(),
                    defaultedDropList);
        }
    }
}
