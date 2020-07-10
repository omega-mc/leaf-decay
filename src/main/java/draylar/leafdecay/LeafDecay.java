package draylar.leafdecay;

import draylar.leafdecay.scheduler.LeafBreakHandler;
import net.fabricmc.api.ModInitializer;

public class LeafDecay implements ModInitializer {

	@Override
	public void onInitialize() {
		LeafBreakHandler.init();
	}
}