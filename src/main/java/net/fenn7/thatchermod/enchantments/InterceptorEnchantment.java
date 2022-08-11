package net.fenn7.thatchermod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;

public class InterceptorEnchantment extends Enchantment {
    protected InterceptorEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() { return 3; }

    @Override
    public boolean isTreasure() { return true; }

    public int getMinPower(int level) {
        return level * 15;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 30;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return (stack.getItem() instanceof ElytraItem);
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (attacker != user && user.distanceTo(attacker) >= 2.0F) {
            for (int i = 0; i < level; i++) {
                PersistentProjectileEntity arrow = ProjectileUtil.createArrowProjectile(user, new ItemStack(Items.ARROW), 1.0F);
                arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                double velX = attacker.getX() - user.getX();
                double velY = attacker.getBodyY((double) 1 / 3) - arrow.getY();
                double velZ = attacker.getZ() - user.getZ();
                double g = Math.sqrt(Math.pow(velX, 2) + Math.pow(velZ, 2));
                arrow.setVelocity(velX, velY + g * 0.2D, velZ, 1.6F, 2.0F);
                user.world.spawnEntity(arrow);
            }
        }
        super.onUserDamaged(user, attacker, level);
    }
}
