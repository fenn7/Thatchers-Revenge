package net.fenn7.thatchermod.commonside.network;

import dev.architectury.networking.NetworkManager;
import net.fenn7.thatchermod.client.network.packet.CSceptreC2SPacket;
import net.fenn7.thatchermod.client.network.packet.GLauncherC2SPacket;
import net.fenn7.thatchermod.client.network.packet.LaunchC2SPacket;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier LAUNCH_ID = new Identifier(ThatcherMod.MOD_ID, "launch");
    public static final Identifier GL_C2S_ID = new Identifier(ThatcherMod.MOD_ID, "gl_c2s");
    public static final Identifier GL_S2C_ID = new Identifier(ThatcherMod.MOD_ID, "gl_s2c");
    public static final Identifier CS_C2S_ID = new Identifier(ThatcherMod.MOD_ID, "cs_c2s");

    public static void registerC2SPackets() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), LAUNCH_ID, LaunchC2SPacket::receive);
        NetworkManager.registerReceiver(NetworkManager.c2s(), GL_C2S_ID, GLauncherC2SPacket::receive);
        NetworkManager.registerReceiver(NetworkManager.c2s(), CS_C2S_ID, CSceptreC2SPacket::receive);
    }
}
