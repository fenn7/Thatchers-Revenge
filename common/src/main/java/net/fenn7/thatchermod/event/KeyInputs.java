package net.fenn7.thatchermod.event;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.netty.buffer.Unpooled;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class KeyInputs {

    private static final String KEY_CATEGORY = "key.thatchermod.category";
    private static final String KEY_LAUNCH = "key.thatchermod.launch";

    private static final KeyBinding LAUNCH_KEY = new KeyBinding(
            KEY_LAUNCH,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_ENTER,
            KEY_CATEGORY
    );

    public static void registerKeys() {
        ClientTickEvent.CLIENT_POST.register(client -> {
            if (LAUNCH_KEY.wasPressed()) {
                PlayerEntity player = client.player;
                if (player != null) {
                    ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
                    int jLevel = EnchantmentHelper.getLevel(ModEnchantments.JET_ASSIST.get(), chest);
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
                        NetworkManager.sendToServer(ModPackets.LAUNCH_ID, new PacketByteBuf(Unpooled.buffer()));
                    }
                }
            }
        });
    }

    public static void register() {
        KeyMappingRegistry.register(LAUNCH_KEY);
        registerKeys();
    }
}
