package net.fenn7.thatchermod.mixin;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity implements TridentInterface {

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"))
    public void injectOnHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        TridentEntity trident = (TridentEntity) (Object) this;
        ItemStack tridentStack = ((TridentInterface) trident).asItemStack();
        Entity thrower = trident.getOwner();
        Entity target = entityHitResult.getEntity();
        ThatcherMod.LOGGER.warn("UH OH !!!");
        //if (thrower != null && target != null && thrower.isWet()) {
        ThatcherMod.LOGGER.warn("UH OH !!!");
            int power = EnchantmentHelper.getLevel(ModEnchantments.TORPEDO, tridentStack);
            if (power != 0) {
                ThatcherMod.LOGGER.warn("" + power);
                ThatcherMod.LOGGER.warn("UH OH !!!");
                target.getWorld().createExplosion(null, target.getX(), target.getY(), target.getZ(), power, Explosion.DestructionType.NONE);
            //}
        }
    }
}
