package net.fenn7.thatchermod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.network.packet.LaunchC2SPacket;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier LAUNCH_ID = new Identifier(ThatcherMod.MOD_ID, "launch");
    public static final Identifier LAUNCH_MOTION_ID = new Identifier(ThatcherMod.MOD_ID, "launch_motion");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(LAUNCH_ID, LaunchC2SPacket::receive);
    }

    public static void registerS2CPackets() {
    }
}
