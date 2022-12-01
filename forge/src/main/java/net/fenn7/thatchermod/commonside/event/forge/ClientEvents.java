package net.fenn7.thatchermod.commonside.event.forge;

import net.fenn7.thatchermod.client.ThatcherModClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
        ThatcherModClient.registerGeoArmorRenderers();
    }
}
