package net.fenn7.thatchermod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SetCooldownCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("cooldowns")
                .then(CommandManager.literal("set").executes(SetCooldownCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
        int unionBusterCD = player.getPersistentData().getInt("union.buster.cd");

        context.getSource().getPlayer().getItemCooldownManager().set(ModItems.UNION_BUSTER, unionBusterCD);

        // for testing purposes
        context.getSource().sendFeedback(Text.literal("All Cooldowns Set!"), true);
        context.getSource().sendFeedback(Text.literal("Union Buster: " + unionBusterCD / 20 + " seconds"), true);
        return 1;
    }
}
