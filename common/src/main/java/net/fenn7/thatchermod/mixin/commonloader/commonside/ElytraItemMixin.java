package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.fenn7.thatchermod.commonside.enchantments.ModEnchantments;
import net.fenn7.thatchermod.commonside.util.ModText;
import net.fenn7.thatchermod.commonside.util.NBTInterface;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ElytraItem.class)
public abstract class ElytraItemMixin extends ItemMixin {

    @Override
    protected void thatchersRevenge$elytraBomb(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        if (clickType == ClickType.RIGHT && EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT.get(), stack) != 0) {
            // right click in inventory to arm/disarm bombs
            NBTInterface.setBombing(stack, !NBTInterface.isBombing(stack));
            cir.setReturnValue(true);
        }
    }

    @Override
    protected void thatchersRevenge$bombingTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT.get(), stack) != 0) {
            if (NBTInterface.isBombing(stack)) {
                tooltip.add(ModText.literal("§4ARMED"));
            } else {
                tooltip.add(ModText.literal("Disarmed"));
            }
        }
    }
}
