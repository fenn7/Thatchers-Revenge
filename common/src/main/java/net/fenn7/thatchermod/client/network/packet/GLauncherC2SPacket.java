package net.fenn7.thatchermod.client.network.packet;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.grenade.GrenadeLauncherItem;
import net.fenn7.thatchermod.commonside.network.ModPackets;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
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
                NetworkManager.sendToPlayer(serverPlayer, ModPackets.GL_S2C_ID, new PacketByteBuf(Unpooled.buffer()));
            }
        }
    }
}
