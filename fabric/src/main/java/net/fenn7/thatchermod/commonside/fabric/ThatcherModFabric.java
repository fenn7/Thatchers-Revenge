package net.fenn7.thatchermod.commonside.fabric;

import net.fabricmc.api.ModInitializer;
import net.fenn7.thatchermod.commonside.ThatcherMod;

public class ThatcherModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ThatcherMod.init();
    }
}
