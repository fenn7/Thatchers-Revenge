package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.fenn7.thatchermod.commonside.enchantments.ModEnchantments;
import net.fenn7.thatchermod.mixin.commonloader.commonside.accessor.TridentAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity implements TridentAccessor {

    @Shadow
    private ItemStack tridentStack;

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void thatchersRevenge$injectOnHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        TridentEntity trident = (TridentEntity) (Object) this;
        Entity thrower = trident.getOwner();
        Entity target = entityHitResult.getEntity();
        if (thrower != null && target != null) {
            if (thrower.isWet()) {
                int torpedoPower = EnchantmentHelper.getLevel(ModEnchantments.TORPEDO.get(), tridentStack);
                if (torpedoPower != 0) {
                    world.createExplosion(null, target.getX(), target.getBodyY(0.5D), target.getZ(), 1F + ((torpedoPower - 1) * 0.75F),
                            Explosion.DestructionType.NONE);
                    target.damage(DamageSource.trident(trident, thrower), torpedoPower * 1.5F);
                }
                int amphPower = EnchantmentHelper.getLevel(ModEnchantments.AMPHIBIOUS.get(), tridentStack);
                if (amphPower != 0) {
                    target.damage(DamageSource.GENERIC, amphPower * 2.5F);
                    if (thrower instanceof LivingEntity living) {
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 1, false, false, false));
                    }
                }
            }
            int hungerPower = EnchantmentHelper.getLevel(ModEnchantments.HUNGERING_STRIKE.get(), tridentStack);
            if (hungerPower != 0) {
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 10, hungerPower - 1, false, false, false));
                }
            }
        }
    }
}
