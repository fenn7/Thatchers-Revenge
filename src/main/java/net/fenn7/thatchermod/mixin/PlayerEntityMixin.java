package net.fenn7.thatchermod.mixin;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.LastStandEffect;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.item.custom.ThatcheriteArmourItem;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Thatcherite Armour effects are handled here.
    // HELMET: +50% experience gained, +1 amplifier level and +25% duration to any beneficial effects applied.
    // CHEST: -50% damage from explosions, gain a health boost and speed when damaged by one.
    // LEGS: Apply a slowness effect to the attacker (level increases the higher the damage taken).
    // BOOTS: Reduce damage by 20%, and reflect 33% of it, if moving in the opposite direction as the attacker.

    @Inject(method = "damage", at = @At("HEAD"))
    public void injectDamageMethod(DamageSource source, float amount, CallbackInfoReturnable cir) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (ThatcheriteArmourItem.hasChest(player) && source.isExplosive()) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60, 1), player);
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60), player);
            amount = (amount / 2);
        }
        if (source.getAttacker() instanceof LivingEntity attacker && attacker != player) {
            if (ThatcheriteArmourItem.hasLegs(player)) {
                attacker.setStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40,
                        (int) (amount/4) - 1), player);
            }
            if (ThatcheriteArmourItem.hasBoots(player)) {
                Direction enemyDirection = attacker.getMovementDirection();
                Direction playerDirection = player.getMovementDirection();
                if (enemyDirection == playerDirection.getOpposite()) {
                    attacker.damage(DamageSource.GENERIC, amount/3);
                    amount -= (amount / 5);
                }
            }
        }
    }

    @Inject(method = "addExperience", at = @At("HEAD"))
    public void injectExperienceMethod(int experience, CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (ThatcheriteArmourItem.hasHelmet(player)) {
            experience = (int) (1.5 * experience);
        }
    }

    @Override
    public boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (ThatcheriteArmourItem.hasHelmet(player) && effect.getEffectType().isBeneficial()) {
            int newLevel = effect.getAmplifier() + 1;
            effect = new StatusEffectInstance(effect.getEffectType(), (int) (1.25 * effect.getDuration()), newLevel,
                    effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon());
        }
        return super.addStatusEffect(effect, source);
    }

    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (player.hasStatusEffect(ModEffects.LAST_STAND)) {
            LastStandEffect.setStatusOnRemove(player, true);
            player.removeStatusEffect(ModEffects.LAST_STAND);

            float newHealth = other.getMaxHealth();
            if (newHealth > 20) { newHealth = 20; }
            player.setHealth(newHealth);

            LastStandEffect.setStatusOnRemove(player, false);
            player.playSound(SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.HOSTILE, 100F, 2.0F);
        }
        return super.onKilledOther(world, other);
    }
}


