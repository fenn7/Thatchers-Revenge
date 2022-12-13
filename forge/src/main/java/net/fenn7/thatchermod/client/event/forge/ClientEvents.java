package net.fenn7.thatchermod.client.event.forge;

import net.fenn7.thatchermod.client.ThatcherModClient;
import net.fenn7.thatchermod.client.event.RenderEvents;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
        ThatcherModClient.registerGeoArmorRenderers();
    }

    @SubscribeEvent
    public static void renderEvents(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            RenderEvents.afterRender();
        }
    }
}
