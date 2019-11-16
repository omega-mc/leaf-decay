package com.github.draylar.leafdecay;

import com.github.draylar.leafdecay.scheduler.LeafBreakHandler;
import net.fabricmc.api.ModInitializer;

public class LeafDecay implements ModInitializer {

	@Override
	public void onInitialize() {
		LeafBreakHandler.init();
	}
}