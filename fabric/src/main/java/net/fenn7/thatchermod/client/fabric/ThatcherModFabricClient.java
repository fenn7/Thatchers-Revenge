package net.fenn7.thatchermod.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fenn7.thatchermod.client.ThatcherModClient;
import net.fenn7.thatchermod.client.event.RenderEvents;

@Environment(EnvType.CLIENT)
public class ThatcherModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ThatcherModClient.init();
        ThatcherModClient.registerGeoArmorRenderers();
        WorldRenderEvents.END.register(context -> RenderEvents.afterRender());
    }
}
