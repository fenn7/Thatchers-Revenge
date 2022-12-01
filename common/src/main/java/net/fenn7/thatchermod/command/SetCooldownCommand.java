package net.fenn7.thatchermod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.fenn7.thatchermod.util.ModText;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class SetCooldownCommand implements Command<ServerCommandSource> {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cooldowns")
                .then(CommandManager.literal("set").executes(new SetCooldownCommand())));
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
        int unionBusterCD = player.getPersistentData().getInt("union.buster.cd");

        context.getSource().getPlayer().getItemCooldownManager().set(ModItems.UNION_BUSTER.get(), unionBusterCD);

        // for testing purposes
        context.getSource().sendFeedback(ModText.literal("All Cooldowns Set!"), true);
        context.getSource().sendFeedback(ModText.literal("Union Buster: " + unionBusterCD / 20 + " seconds"), true);
        return 1;
    }
}
