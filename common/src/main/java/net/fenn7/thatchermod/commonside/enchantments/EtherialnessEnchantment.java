package net.fenn7.thatchermod.commonside.enchantments;

import net.fenn7.thatchermod.commonside.item.custom.CommandSceptreItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class EtherialnessEnchantment extends Enchantment {
    protected EtherialnessEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    public int getMinPower(int level) {
        return level * 25;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return (stack.getItem() instanceof CommandSceptreItem);
    }
}
