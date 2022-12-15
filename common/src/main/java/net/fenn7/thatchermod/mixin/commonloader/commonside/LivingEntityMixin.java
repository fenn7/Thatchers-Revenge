package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract void setStatusEffect(StatusEffectInstance effect, @Nullable Entity source);

    @Shadow
    public abstract boolean canSee(Entity entity);

    @Shadow
    public abstract boolean isFallFlying();

    @Shadow
    public abstract int getRoll();

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract void takeKnockback(double strength, double x, double z);

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"))
    protected void thatchersRevenge$buffStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
    }
}
