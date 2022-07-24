package net.fenn7.thatchermod.mixin;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.CommunityChargebowItem;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.TridentItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
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
    protected void injectEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) throws Exception {
        Entity entity = entityHitResult.getEntity();
        PersistentProjectileEntity projectile = ((PersistentProjectileEntity) (Object) this);
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
                if (attacker instanceof PlayerEntity player /*&& (player.getMainHandStack().getItem() instanceof TridentItem
                        || player.getOffHandStack().getItem() instanceof TridentItem) && projectile instanceof TridentEntity*/) {
                    if (player.isWet()) {
                        int power = Math.max(EnchantmentHelper.getLevel(ModEnchantments.TORPEDO, player.getMainHandStack()),
                                EnchantmentHelper.getLevel(ModEnchantments.TORPEDO, player.getOffHandStack()));
                        world.createExplosion(living, living.getX(), living.getY(), living.getZ(), power, Explosion.DestructionType.NONE);
                    }
                }
            }
        }
    }
}
