package net.fenn7.thatchermod;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fenn7.thatchermod.command.GetCooldownCommand;
import net.fenn7.thatchermod.command.SetCooldownCommand;

public class ModCommands {

    public static void registerAllCommands() {
        CommandRegistrationCallback.EVENT.register(SetCooldownCommand::register);
        CommandRegistrationCallback.EVENT.register(GetCooldownCommand::register);
    }
}
