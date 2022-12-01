package net.fenn7.thatchermod.commonside;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.fenn7.thatchermod.commonside.command.GetCooldownCommand;
import net.fenn7.thatchermod.commonside.command.SetCooldownCommand;

public class ModCommands {

    public static void registerAllCommands() {
        CommandRegistrationEvent.EVENT.register((dispatcher, commandRegistryAccess) -> SetCooldownCommand.register(dispatcher));
        CommandRegistrationEvent.EVENT.register((dispatcher, commandRegistryAccess) -> GetCooldownCommand.register(dispatcher));
    }
}
