package com.github.draylar.leafDecay;

import com.github.draylar.leafDecay.scheduler.LeafBreakHandler;
import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class LeafDecay implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		AutoConfig.register(LeafDecayConfig.class, GsonConfigSerializer::new);
		LeafBreakHandler.init();
	}
}
