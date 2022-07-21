package net.fenn7.thatchermod;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fenn7.thatchermod.event.BlockBreakEvents;
import net.fenn7.thatchermod.event.PlayConnectionEvents;
import net.fenn7.thatchermod.event.PlayerEvents;
import net.fenn7.thatchermod.event.RenderEvents;

public class ModEvents {

    public static void registerAllEvents() {
        ServerPlayConnectionEvents.JOIN.register(new PlayConnectionEvents());
        ServerPlayConnectionEvents.DISCONNECT.register(new PlayConnectionEvents());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerEvents());
        ServerPlayerEvents.ALLOW_DEATH.register(new PlayerEvents());
        PlayerBlockBreakEvents.AFTER.register(new BlockBreakEvents());
        WorldRenderEvents.END.register(new RenderEvents());
    }
}
