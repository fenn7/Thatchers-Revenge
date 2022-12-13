package net.fenn7.thatchermod.client.event.forge;

import net.fenn7.thatchermod.client.event.RenderEvents;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ThatcherMod.MOD_ID,value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void renderEvents(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            RenderEvents.afterRender();
        }
    }
}
