package net.fenn7.thatchermod.network.packet;

import dev.architectury.networking.NetworkManager;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.entity.projectiles.SmokeEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class LaunchC2SPacket {

    public static void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        int jLevel = EnchantmentHelper.getLevel(ModEnchantments.JET_ASSIST.get(), chest);

        if (player.isFallFlying() && chest.isOf(Items.ELYTRA) && ElytraItem.isUsable(chest) && jLevel != 0) {
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.PLAYERS,
                    1.0F + jLevel * 0.2F, 0.75F);
            SmokeEntity smoke = new SmokeEntity(player.getWorld(), player, player.getX(), player.getBodyY(0.001F), player.getZ());
            player.getWorld().spawnEntity(smoke);
        }
    }
}
