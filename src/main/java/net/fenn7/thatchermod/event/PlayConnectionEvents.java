package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

public class PlayConnectionEvents implements ServerPlayConnectionEvents.Join, ServerPlayConnectionEvents.Disconnect {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        IEntityDataSaver player = (IEntityDataSaver) ((PlayerEntity) handler.getPlayer());
        int unionBusterCD = player.getPersistentData().getInt("union.buster.cd");

        handler.getPlayer().getItemCooldownManager().set(ModItems.UNION_BUSTER, unionBusterCD);
    }

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        IEntityDataSaver player = ((IEntityDataSaver) ((PlayerEntity) handler.getPlayer()));
        int unionBusterCD = (int) (UnionBusterItem.DURATION * handler.getPlayer().getItemCooldownManager().
                getCooldownProgress(ModItems.UNION_BUSTER, 0));

        player.getPersistentData().putInt("union.buster.cd", unionBusterCD);
    }
}
