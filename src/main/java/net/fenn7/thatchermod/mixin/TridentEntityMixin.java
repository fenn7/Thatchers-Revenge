package net.fenn7.thatchermod.mixin;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.fenn7.thatchermod.mixin.TridentInterface;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity implements TridentInterface {
    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"))
    public void injectOnHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        TridentEntity trident = (TridentEntity) (Object) this;
        ItemStack tridentStack = ((TridentInterface) this).callAsItemStack();
        Entity thrower = trident.getOwner();
        Entity target = entityHitResult.getEntity();
        if (thrower != null && target != null) {
            if (thrower.isWet()) {
                int torpedoPower = EnchantmentHelper.getLevel(ModEnchantments.TORPEDO, tridentStack);
                if (torpedoPower != 0) {
                    world.createExplosion(null, target.getX(), target.getY(), target.getZ(), 1F + ((torpedoPower - 1) * 0.75F),
                            Explosion.DestructionType.NONE);
                }
                int amphPower = EnchantmentHelper.getLevel(ModEnchantments.AMPHIBIOUS, tridentStack);
                if (amphPower != 0) {
                    target.damage(DamageSource.GENERIC, amphPower * 2.5F);
                    if (thrower instanceof LivingEntity living) {
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 1, false, false, false));
                    }
                }
            }
            int hungerPower = EnchantmentHelper.getLevel(ModEnchantments.HUNGERING_STRIKE, tridentStack);
            if (hungerPower != 0) {
                if (target instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 10, hungerPower - 1,  false, false, false));
                }
            }
        }
    }
}
