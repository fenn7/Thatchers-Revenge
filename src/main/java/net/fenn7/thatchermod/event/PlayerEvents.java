package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEvents implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        int unionBusterCD = (int) (UnionBusterItem.DURATION *
                oldPlayer.getItemCooldownManager().getCooldownProgress(ModItems.UNION_BUSTER, 0));
        newPlayer.getItemCooldownManager().set(ModItems.UNION_BUSTER, unionBusterCD);
    }
}
