package net.fenn7.thatchermod.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.entity.projectiles.SmokeEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class LaunchC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        int jLevel = EnchantmentHelper.getLevel(ModEnchantments.JET_ASSIST, chest);

        if (player.isFallFlying() && chest.isOf(Items.ELYTRA) && ElytraItem.isUsable(chest) && jLevel != 0) {
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.PLAYERS,
                    1.0F + jLevel * 0.2F, 0.75F);
            SmokeEntity smoke = new SmokeEntity(player.getWorld(), player, player.getX(), player.getBodyY(0.001F), player.getZ());
            player.getWorld().spawnEntity(smoke);
        }
    }
}
