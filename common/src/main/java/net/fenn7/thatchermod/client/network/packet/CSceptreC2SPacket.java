package net.fenn7.thatchermod.client.network.packet;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.entity.projectiles.CursedMissileEntity;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.CommandSceptreItem;
import net.fenn7.thatchermod.commonside.item.custom.grenade.GrenadeLauncherItem;
import net.fenn7.thatchermod.commonside.network.ModPackets;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Set;

public class CSceptreC2SPacket {
    public static void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack mainStack = player.getMainHandStack();
        if (mainStack.isOf(ModItems.COMMAND_SCEPTRE.get()) &&
                (player.isCreative() || player.getInventory().count(Items.LAPIS_LAZULI) >= 1)) {
            CommandSceptreItem sceptreItem = (CommandSceptreItem) mainStack.getItem();
            mainStack.damage(1, player, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            sceptreItem.launchMissileEntity(player.world, player);
        }
    }
}
