package net.fenn7.thatchermod.client.event.forge;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.world.gen.ThatcherModEntityGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ThatcherMod.MOD_ID)
public class EntityForgeEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ThatcherModEntityGeneration.onEntitySpawn(event);
    }
}
