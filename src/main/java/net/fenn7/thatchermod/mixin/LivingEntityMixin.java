package net.fenn7.thatchermod.mixin;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    // this doesn't actually work rn and idk why.
    // maybe in future i will revisit this as a solution but for now it just exists ig

    //private final IEntityDataSaver entityData = (IEntityDataSaver) ((LivingEntity) (Object) this);

    public LivingEntityMixin(EntityType<?> type, World world) { super(type, world); }

    /*@Inject(method = "baseTick", at = @At("HEAD"))
    public void injectBaseTick(CallbackInfo ci) {
        if (!world.isClient) {
        IEntityDataSaver entityData = (IEntityDataSaver) ((LivingEntity) (Object) this);
        int count = entityData.getPersistentData().getInt("times.hit.by.charged");
        if (count == 1) {
            this.world.addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + this.getHeight() + count, this.getZ(),
                    0.0D, 0.0D, 0.0D);
            ThatcherMod.LOGGER.warn("1 HIT A HA HA");
        }
        if (count == 2) {
            this.world.addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + this.getHeight() + count, this.getZ(),
                    0.0D, 0.0D, 0.0D);
            ThatcherMod.LOGGER.warn("2 TW OOOO HIT A HA HA");
        }
        }
    }

    /*@Inject(method = "handleStatus", at = @At("HEAD"))
    public void injectHandleStatus(byte status, CallbackInfo ci) {
        switch (status) {
            case 62: {
                IEntityDataSaver entityData = (IEntityDataSaver) this;
                int count = entityData.getPersistentData().getInt("times.hit.by.charged");
                if (count == 1) {
                    this.world.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY() + this.getHeight() + count, this.getZ(),
                            0.0D, 0.0D, 0.0D);
                }
                else if (count == 2) {
                    this.world.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + this.getHeight() + count, this.getZ(),
                            0.0D, 0.0D, 0.0D);
                }
                else if (count == 3) {
                    LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, this.world);
                    lightning.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(lightning);
                    entityData.getPersistentData().putInt("times.hit.by.charged", 0);
                }
            }
        }
    }*/
}
