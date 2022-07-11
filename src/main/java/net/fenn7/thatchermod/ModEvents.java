package net.fenn7.thatchermod;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fenn7.thatchermod.event.PlayConnectionEvents;

public class ModEvents {

    public static void registerAllEvents() {
        ServerPlayConnectionEvents.JOIN.register(new PlayConnectionEvents());
        ServerPlayConnectionEvents.DISCONNECT.register(new PlayConnectionEvents());
    }
}
