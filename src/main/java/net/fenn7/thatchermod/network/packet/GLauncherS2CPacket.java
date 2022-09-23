package net.fenn7.thatchermod.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class GLauncherS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        IEntityDataSaver data = (IEntityDataSaver) client.player;
        ThatcherMod.LOGGER.warn("COIL");
        data.getPersistentData().putBoolean("should_recoil", true);
    }
}
