package net.fenn7.thatchermod.client.network;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.network.ModPackets;
import net.fenn7.thatchermod.commonside.network.packet.GLauncherS2CPacket;

@Environment(EnvType.CLIENT)
public class ModPacketsClient {

    public static void registerS2CPackets() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), ModPackets.GL_S2C_ID, GLauncherS2CPacket::receive);
    }
}
