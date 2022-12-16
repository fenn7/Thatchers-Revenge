package net.fenn7.thatchermod.commonside.enchantments;

import net.fenn7.thatchermod.commonside.item.custom.CommandSceptreItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnigmaticEnchantment extends Enchantment {
    protected EnigmaticEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() { return 1; }

    @Override
    public boolean isTreasure() {
        return true;
    }

    public int getMinPower(int level) {
        return level * 30;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 20;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return (stack.getItem() instanceof CommandSceptreItem);
    }
}
