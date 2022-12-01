package net.fenn7.thatchermod.network.packet;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.grenade.GrenadeLauncherItem;
import net.fenn7.thatchermod.network.ModPackets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class GLauncherC2SPacket {

    public static void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack mainStack = player.getMainHandStack();
        if (mainStack.isOf(ModItems.GRENADE_LAUNCHER.get())) {
            GrenadeLauncherItem item = ((GrenadeLauncherItem) mainStack.getItem());
            item.setInventory(player, Hand.MAIN_HAND);
            ItemStack grenadeStack = item.getList().get(0);
            if (item.shootGrenade(mainStack, grenadeStack, player.world, player)) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                Packet<?> packet = new CustomPayloadS2CPacket(ModPackets.GL_S2C_ID, new PacketByteBuf(Unpooled.buffer()));
                serverPlayer.networkHandler.sendPacket(packet);
            }
        }
    }
}
