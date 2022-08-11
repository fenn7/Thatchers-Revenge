package net.fenn7.thatchermod.mixin;

import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.util.NBTInterface;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ElytraItem.class)
public abstract class ElytraItemMixin extends Item {
    public ElytraItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT, stack) != 0) {
            if (NBTInterface.isBombing(stack)) { tooltip.add(Text.literal("§4ARMED")); }
            else { tooltip.add(Text.literal("Disarmed")); }
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT, stack) != 0) {
            // right click in inventory to arm/disarm bombs
            if (NBTInterface.isBombing(stack)) { NBTInterface.setBombing(stack, false); }
            else { NBTInterface.setBombing(stack, true); }
            return true;
        }
        else { return false; }
    }
}
