package com.github.draylar.leafDecay.scheduler;

import com.github.draylar.leafDecay.util.LeavesBreaker;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

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
