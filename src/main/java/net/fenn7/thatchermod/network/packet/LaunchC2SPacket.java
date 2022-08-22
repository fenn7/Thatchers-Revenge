package net.fenn7.thatchermod.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fenn7.thatchermod.entity.custom.SmokeEntity;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class LaunchC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int jLevel = EnchantmentHelper.getLevel(ModEnchantments.JET_ASSIST, player.getEquippedStack(EquipmentSlot.CHEST));
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.PLAYERS,
                1.0F + jLevel * 0.2F, 0.75F);
        SmokeEntity smoke = new SmokeEntity(player.getWorld(), player, player.getX(), player.getY(), player.getZ());
        player.getWorld().spawnEntity(smoke);
        /*((ServerWorld) player.getWorld()).spawnParticles(player, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, player.getX(),
                player.getBodyY(0.5D), player.getZ(), 1, 0, 0, 0, 0);
        ((ServerWorld) player.getWorld()).spawnParticles(player, ParticleTypes.FIREWORK, true, player.getX(),
                player.getBodyY(0.5D), player.getZ(), 3, 0, 0, 0, 0);*/
    }
}
