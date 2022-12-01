package net.fenn7.thatchermod.commonside.event;

import dev.architectury.event.events.common.PlayerEvent;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.*;
import net.fenn7.thatchermod.commonside.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public enum PlayConnectionEvents implements PlayerEvent.PlayerJoin, PlayerEvent.PlayerQuit {

    INSTANCE;

    @Override
    public void join(ServerPlayerEntity player) {
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;

        player.getItemCooldownManager().set(ModItems.UNION_BUSTER.get(), dataSaver.getPersistentData().getInt("union.buster.cd"));
        player.getItemCooldownManager().set(ModItems.MILK_SNATCHER.get(), dataSaver.getPersistentData().getInt("milk.snatcher.cd"));
        player.getItemCooldownManager().set(ModItems.COMMUNITY_CHARGEBOW.get(), dataSaver.getPersistentData().getInt("community.chargebow.cd"));
        player.getItemCooldownManager().set(ModItems.COMMAND_SCEPTRE.get(), dataSaver.getPersistentData().getInt("command.sceptre.cd"));
        player.getItemCooldownManager().set(ModItems.COLLIERY_CLOSER.get(), dataSaver.getPersistentData().getInt("colliery.closer.cd"));
    }

    @Override
    public void quit(ServerPlayerEntity player) {
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;
        int unionBusterCD = (int) (UnionBusterItem.DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.UNION_BUSTER.get(), 0));
        int milkSnatcherCD = (int) (MilkSnatcherItem.DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.MILK_SNATCHER.get(), 0));
        int communityBowCD = (int) (CommunityChargebowItem.DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.COMMUNITY_CHARGEBOW.get(), 0));
        int commandSceptreCD = (int) (CommandSceptreItem.METEOR_DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.COMMAND_SCEPTRE.get(), 0));
        int collieryCloserCD = (int) (CollieryCloserItem.DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.COLLIERY_CLOSER.get(), 0));

        dataSaver.getPersistentData().putInt("union.buster.cd", unionBusterCD);
        dataSaver.getPersistentData().putInt("milk.snatcher.cd", milkSnatcherCD);
        dataSaver.getPersistentData().putInt("community.chargebow.cd", communityBowCD);
        dataSaver.getPersistentData().putInt("command.sceptre.cd", commandSceptreCD);
        dataSaver.getPersistentData().putInt("colliery.closer.cd", collieryCloserCD);
    }
}
