package net.fenn7.thatchermod.commonside.network.packet;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.util.ThatcherModEntityData;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class GLauncherS2CPacket {

    public static void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        ThatcherModEntityData data = (ThatcherModEntityData) context.getPlayer();
        data.getPersistentData().putBoolean("should_recoil", true);
    }
}
