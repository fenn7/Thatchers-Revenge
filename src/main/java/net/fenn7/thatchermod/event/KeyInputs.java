package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.network.ModPackets;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class KeyInputs {
    public static final String KEY_CATEGORY = "key.thatchermod.category";
    public static final String KEY_LAUNCH = "key.thatchermod.launch";

    public static KeyBinding launchKey;

    public static void registerKeys() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (launchKey.wasPressed()) {
                PlayerEntity player = client.player;
                ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
                int jLevel = EnchantmentHelper.getLevel(ModEnchantments.JET_ASSIST, chest);
                if (player.isFallFlying() && chest.isOf(Items.ELYTRA) && ElytraItem.isUsable(chest) && jLevel != 0) {
                    float f = player.getYaw();
                    float g = player.getPitch();
                    float h = -MathHelper.sin(f * 0.0175F) * MathHelper.cos(g * 0.0175F);
                    float k = -MathHelper.sin(g * 0.0175F);
                    float l = MathHelper.cos(f * 0.0175F) * MathHelper.cos(g * 0.0175F);
                    float m = MathHelper.sqrt(h * h + k * k + l * l);
                    float n = 0.7F * jLevel + 1.0F;
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;
                    player.addVelocity(h, k, l);
                    chest.damage(10, player, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    ClientPlayNetworking.send(ModPackets.LAUNCH_ID, PacketByteBufs.create());
                }
            }
        });
    }

    public static void register() {
        launchKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_LAUNCH,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_ENTER,
                KEY_CATEGORY
        ));
        registerKeys();
    }
}
