package com.github.draylar.leafDecay;

import com.github.draylar.leafDecay.scheduler.LeafBreakHandler;
import net.fabricmc.api.ModInitializer;

public class LeafDecay implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		LeafBreakHandler.init();
	}
}
