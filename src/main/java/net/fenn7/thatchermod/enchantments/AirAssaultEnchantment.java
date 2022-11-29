package net.fenn7.thatchermod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

public class AirAssaultEnchantment extends Enchantment {
    protected AirAssaultEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    public int getMinPower(int level) {
        return level * 20;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 40;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return (stack.getItem() instanceof ElytraItem);
    }
}
