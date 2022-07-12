package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEvents implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IEntityDataSaver original = ((IEntityDataSaver) oldPlayer);
        IEntityDataSaver player = ((IEntityDataSaver) newPlayer);

        player.getPersistentData().putInt("union.buster.cd", original.getPersistentData().getInt("union.buster.cd"));
        newPlayer.getItemCooldownManager().set(ModItems.UNION_BUSTER, player.getPersistentData().getInt("union.buster.cd"));
    }
}
