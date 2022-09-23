package net.fenn7.thatchermod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.network.packet.GLauncherC2SPacket;
import net.fenn7.thatchermod.network.packet.GLauncherS2CPacket;
import net.fenn7.thatchermod.network.packet.LaunchC2SPacket;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier LAUNCH_ID = new Identifier(ThatcherMod.MOD_ID, "launch");
    public static final Identifier GL_C2S_ID = new Identifier(ThatcherMod.MOD_ID, "gl_c2s");
    public static final Identifier GL_S2C_ID = new Identifier(ThatcherMod.MOD_ID, "gl_s2c");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(LAUNCH_ID, LaunchC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GL_C2S_ID, GLauncherC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(GL_S2C_ID, GLauncherS2CPacket::receive);
    }
}
