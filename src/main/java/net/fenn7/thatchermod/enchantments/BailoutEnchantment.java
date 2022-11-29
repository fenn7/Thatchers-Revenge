package net.fenn7.thatchermod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class BailoutEnchantment extends Enchantment {
    protected BailoutEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    public int getMinPower(int level) {
        return level * 25;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }
}
