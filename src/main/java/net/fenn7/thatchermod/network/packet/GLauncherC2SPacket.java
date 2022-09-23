package net.fenn7.thatchermod.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.SmokeEntity;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.grenade.AbstractGrenadeItem;
import net.fenn7.thatchermod.item.custom.grenade.GrenadeLauncherItem;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class GLauncherC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        ItemStack mainStack = player.getMainHandStack();
        if (mainStack.isOf(ModItems.GRENADE_LAUNCHER)) {
            GrenadeLauncherItem item = ((GrenadeLauncherItem) mainStack.getItem());
            ItemStack grenadeStack = item.getList().get(0);
            item.shootGrenade(grenadeStack, player.world, player);
        }
    }
}
