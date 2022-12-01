package net.fenn7.thatchermod.network.packet;

import dev.architectury.networking.NetworkManager;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.network.PacketByteBuf;

public class GLauncherS2CPacket {

    public static void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        IEntityDataSaver data = (IEntityDataSaver) context.getPlayer();
        data.getPersistentData().putBoolean("should_recoil", true);
    }
}
