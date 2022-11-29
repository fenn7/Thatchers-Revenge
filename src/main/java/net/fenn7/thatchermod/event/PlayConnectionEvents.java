package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.*;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PlayConnectionEvents implements ServerPlayConnectionEvents.Join, ServerPlayConnectionEvents.Disconnect {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        IEntityDataSaver player = (IEntityDataSaver) handler.getPlayer();

        handler.getPlayer().getItemCooldownManager().set(ModItems.UNION_BUSTER, player.getPersistentData().getInt("union.buster.cd"));
        handler.getPlayer().getItemCooldownManager().set(ModItems.MILK_SNATCHER, player.getPersistentData().getInt("milk.snatcher.cd"));
        handler.getPlayer().getItemCooldownManager().set(ModItems.COMMUNITY_CHARGEBOW, player.getPersistentData().getInt("community.chargebow.cd"));
        handler.getPlayer().getItemCooldownManager().set(ModItems.COMMAND_SCEPTRE, player.getPersistentData().getInt("command.sceptre.cd"));
        handler.getPlayer().getItemCooldownManager().set(ModItems.COLLIERY_CLOSER, player.getPersistentData().getInt("colliery.closer.cd"));
    }

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        IEntityDataSaver player = ((IEntityDataSaver) handler.getPlayer());
        int unionBusterCD = (int) (UnionBusterItem.DURATION * handler.getPlayer().getItemCooldownManager().
                getCooldownProgress(ModItems.UNION_BUSTER, 0));
        int milkSnatcherCD = (int) (MilkSnatcherItem.DURATION * handler.getPlayer().getItemCooldownManager().
                getCooldownProgress(ModItems.MILK_SNATCHER, 0));
        int communityBowCD = (int) (CommunityChargebowItem.DURATION * handler.getPlayer().getItemCooldownManager().
                getCooldownProgress(ModItems.COMMUNITY_CHARGEBOW, 0));
        int commandSceptreCD = (int) (CommandSceptreItem.METEOR_DURATION * handler.getPlayer().getItemCooldownManager().
                getCooldownProgress(ModItems.COMMAND_SCEPTRE, 0));
        int collieryCloserCD = (int) (CollieryCloserItem.DURATION * handler.getPlayer().getItemCooldownManager().
                getCooldownProgress(ModItems.COLLIERY_CLOSER, 0));

        player.getPersistentData().putInt("union.buster.cd", unionBusterCD);
        player.getPersistentData().putInt("milk.snatcher.cd", milkSnatcherCD);
        player.getPersistentData().putInt("community.chargebow.cd", communityBowCD);
        player.getPersistentData().putInt("command.sceptre.cd", commandSceptreCD);
        player.getPersistentData().putInt("colliery.closer.cd", collieryCloserCD);
    }
}
