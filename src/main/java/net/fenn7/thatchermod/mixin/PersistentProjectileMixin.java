package net.fenn7.thatchermod.mixin;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.CommunityChargebowItem;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileMixin extends Entity {
    public PersistentProjectileMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"))
    protected void injectEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity living) {
            Entity attacker = ((PersistentProjectileEntity) (Object) this).getOwner();
            if (attacker != null && attacker.getItemsHand() != null) {
                if (attacker instanceof PlayerEntity player && (player.getMainHandStack().getItem() == ModItems.COMMUNITY_CHARGEBOW
                    || player.getOffHandStack().getItem() == ModItems.COMMUNITY_CHARGEBOW)) {
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
    }
}
