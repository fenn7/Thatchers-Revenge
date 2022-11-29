package net.fenn7.thatchermod;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fenn7.thatchermod.command.GetCooldownCommand;
import net.fenn7.thatchermod.command.SetCooldownCommand;

public class ModCommands {

    public static void registerAllCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess1) -> SetCooldownCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess) -> GetCooldownCommand.register(dispatcher));
    }
}
