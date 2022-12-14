package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.fenn7.thatchermod.commonside.effect.ModEffects;
import net.fenn7.thatchermod.commonside.util.ThatcherModEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.fenn7.thatchermod.commonside.effect.StaticBuildupEffect.SHOULD_STRIKE;
import static net.fenn7.thatchermod.commonside.item.custom.CommunityChargebowItem.LIGHTNING_CHARGE;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileMixin extends Entity {
    public PersistentProjectileMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"))
    protected void injectEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity entity = entityHitResult.getEntity();
        PersistentProjectileEntity projectile = ((PersistentProjectileEntity) (Object) this);
        ThatcherModEntityData projData = (ThatcherModEntityData) projectile;
        if (projData.getPersistentData().getBoolean(LIGHTNING_CHARGE) && entity instanceof LivingEntity living) {
            ThatcherModEntityData entityData = (ThatcherModEntityData) entity;
            int count = entityData.getPersistentData().getInt("times.hit.by.charged");
            if (count < 2) { // 1 less than the number of hits to spawn lightning!
                entityData.getPersistentData().putInt("times.hit.by.charged", count + 1);
                living.addStatusEffect(new StatusEffectInstance(ModEffects.STATIC_BUILDUP.get(), 1200, count, false, false));
            } else {
                entityData.getPersistentData().putInt("times.hit.by.charged", 0);
                entityData.getPersistentData().putBoolean(SHOULD_STRIKE, true);
                living.removeStatusEffect(ModEffects.STATIC_BUILDUP.get());
                entityData.getPersistentData().putBoolean(SHOULD_STRIKE, false);
            }
        }

        /*
        if (entity instanceof LivingEntity living) {
            Entity attacker = projectile.getOwner();
            if (attacker != null && attacker.getItemsHand() != null) {
                if (attacker instanceof PlayerEntity player && (player.getMainHandStack().getItem() == ModItems.COMMUNITY_CHARGEBOW
                    || player.getOffHandStack().getItem() == ModItems.COMMUNITY_CHARGEBOW
                        && (projectile instanceof SpectralArrowEntity || projectile instanceof ArrowEntity))) {
                    IEntityDataSaver entityData = (IEntityDataSaver) entityHitResult.getEntity();
                    int count = entityData.getPersistentData().getInt("times.hit.by.charged");
                    if (count < 2) { // 1 less than the number of hits to spawn lightning!
                        living.addStatusEffect(new StatusEffectInstance(ModEffects.STATIC_BUILDUP, 1200, count, false, false));
                        entityData.getPersistentData().putInt("times.hit.by.charged", count + 1);
                    } else {
                        living.removeStatusEffect(ModEffects.STATIC_BUILDUP);
                        entityData.getPersistentData().putInt("times.hit.by.charged", 0);
                    }
                }
            }
        }
        */
    }
}
