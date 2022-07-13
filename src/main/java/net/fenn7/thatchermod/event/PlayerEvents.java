package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEvents implements ServerPlayerEvents.AfterRespawn {
    /*@Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IEntityDataSaver original = ((IEntityDataSaver) oldPlayer);
        IEntityDataSaver player = ((IEntityDataSaver) newPlayer);

        player.getPersistentData().putInt("union.buster.cd", original.getPersistentData().getInt("union.buster.cd"));
        newPlayer.getItemCooldownManager().set(ModItems.UNION_BUSTER, player.getPersistentData().getInt("union.buster.cd"));
    }*/

    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        int unionBusterCD = (int) (UnionBusterItem.DURATION *
                oldPlayer.getItemCooldownManager().getCooldownProgress(ModItems.UNION_BUSTER, 0));
        newPlayer.getItemCooldownManager().set(ModItems.UNION_BUSTER, unionBusterCD);
    }
}
