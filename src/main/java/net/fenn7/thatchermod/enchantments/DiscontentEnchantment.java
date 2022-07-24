package net.fenn7.thatchermod.enchantments;

import net.fenn7.thatchermod.effect.ModEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class DiscontentEnchantment extends Enchantment {
    protected DiscontentEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() { return 2; }

    public int getMinPower(int level) {
        return level * 10;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 40;
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        attacker.setFrozenTicks(240);
        if (attacker instanceof LivingEntity living && attacker != user) {
            living.addStatusEffect(new StatusEffectInstance(ModEffects.DISCONTENT, (level + 1) * 20, level - 1
            , false, false, true));
        }
        super.onUserDamaged(user, attacker, level);
    }
}
