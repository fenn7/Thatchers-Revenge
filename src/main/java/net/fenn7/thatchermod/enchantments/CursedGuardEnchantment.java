package net.fenn7.thatchermod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class CursedGuardEnchantment extends Enchantment {
    protected CursedGuardEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() { return 3; }

    public int getMinPower(int level) {
        return level * 10;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 40;
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (attacker instanceof LivingEntity living && attacker != user) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 40, level - 1,
                    false, false, true));
            if (living.squaredDistanceTo(user) <= 2.0D) {
                living.takeKnockback(level - 1, living.prevX, living.prevZ);
            }
        }
        super.onUserDamaged(user, attacker, level);
    }
}
