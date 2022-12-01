package net.fenn7.thatchermod.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.ThatcherModClient;

@Environment(EnvType.CLIENT)
public class ThatcherModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ThatcherModClient.init();
        ThatcherModClient.registerGeoArmorRenderers();
    }
}
