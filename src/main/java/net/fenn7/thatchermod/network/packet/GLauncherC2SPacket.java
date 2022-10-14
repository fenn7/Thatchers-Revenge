package net.fenn7.thatchermod.network.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.grenade.GrenadeLauncherItem;
import net.fenn7.thatchermod.network.ModPackets;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class GLauncherC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        ItemStack mainStack = player.getMainHandStack();
        if (mainStack.isOf(ModItems.GRENADE_LAUNCHER)) {
            GrenadeLauncherItem item = ((GrenadeLauncherItem) mainStack.getItem());
            item.setInventory(player, Hand.MAIN_HAND);
            ItemStack grenadeStack = item.getList().get(0);
            if (item.shootGrenade(mainStack, grenadeStack, player.world, player)) {
                ServerPlayNetworking.send(player, ModPackets.GL_S2C_ID, PacketByteBufs.create());
            }
        }
    }
}
