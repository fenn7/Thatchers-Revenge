package net.fenn7.thatchermod;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.fenn7.thatchermod.command.GetCooldownCommand;
import net.fenn7.thatchermod.command.SetCooldownCommand;

public class ModCommands {

    public static void registerAllCommands() {
        CommandRegistrationEvent.EVENT.register((dispatcher, commandRegistryAccess) -> SetCooldownCommand.register(dispatcher));
        CommandRegistrationEvent.EVENT.register((dispatcher, commandRegistryAccess) -> GetCooldownCommand.register(dispatcher));
    }
}
