package com.github.draylar.leafDecay;

import me.sargunvohra.mcmods.autoconfig1.ConfigData;
import me.sargunvohra.mcmods.autoconfig1.annotation.Config;

@Config(name = "leaf-decay")
public class LeafDecayConfig implements ConfigData
{
    public boolean leafParticles = false;
    public boolean playSound = false;
}
