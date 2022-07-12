package net.fenn7.thatchermod;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fenn7.thatchermod.event.PlayConnectionEvents;
import net.fenn7.thatchermod.event.PlayerEvents;

public class ModEvents {

    public static void registerAllEvents() {
        ServerPlayConnectionEvents.JOIN.register(new PlayConnectionEvents());
        ServerPlayConnectionEvents.DISCONNECT.register(new PlayConnectionEvents());
        ServerPlayerEvents.COPY_FROM.register(new PlayerEvents());
    }
}
