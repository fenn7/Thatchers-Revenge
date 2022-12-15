package net.fenn7.thatchermod.commonside.event;

import dev.architectury.event.events.common.PlayerEvent;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.*;
import net.fenn7.thatchermod.commonside.util.ThatcherModEntityData;
import net.minecraft.server.network.ServerPlayerEntity;

public enum PlayConnectionEvents implements PlayerEvent.PlayerJoin, PlayerEvent.PlayerQuit {

    INSTANCE;

    @Override
    public void join(ServerPlayerEntity player) {
        ThatcherModEntityData dataSaver = (ThatcherModEntityData) player;

        player.getItemCooldownManager().set(ModItems.UNION_BUSTER.get(), dataSaver.thatchersRevenge$getPersistentData().getInt("union.buster.cd"));
        player.getItemCooldownManager().set(ModItems.MILK_SNATCHER.get(), dataSaver.thatchersRevenge$getPersistentData().getInt("milk.snatcher.cd"));
        player.getItemCooldownManager().set(ModItems.COLLIERY_CLOSER.get(), dataSaver.thatchersRevenge$getPersistentData().getInt("colliery.closer.cd"));
    }

    @Override
    public void quit(ServerPlayerEntity player) {
        ThatcherModEntityData dataSaver = (ThatcherModEntityData) player;
        int unionBusterCD = (int) (UnionBusterItem.DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.UNION_BUSTER.get(), 0));
        int milkSnatcherCD = (int) (MilkSnatcherItem.DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.MILK_SNATCHER.get(), 0));
        int collieryCloserCD = (int) (CollieryCloserItem.DURATION * player.getItemCooldownManager().
                getCooldownProgress(ModItems.COLLIERY_CLOSER.get(), 0));

        dataSaver.thatchersRevenge$getPersistentData().putInt("union.buster.cd", unionBusterCD);
        dataSaver.thatchersRevenge$getPersistentData().putInt("milk.snatcher.cd", milkSnatcherCD);
        dataSaver.thatchersRevenge$getPersistentData().putInt("colliery.closer.cd", collieryCloserCD);
    }
}
