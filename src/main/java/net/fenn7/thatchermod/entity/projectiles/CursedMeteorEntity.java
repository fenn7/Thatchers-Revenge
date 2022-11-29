package net.fenn7.thatchermod.entity.projectiles;

import net.fenn7.thatchermod.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Iterator;
import java.util.List;

public class CursedMeteorEntity extends ExplosiveProjectileEntity {
    private static final float explosionPower = 2.5F;
    private static final int maximumAgeTicks = 300;
    private static boolean isFalling = false;

    public CursedMeteorEntity(EntityType<? extends CursedMeteorEntity> entityType, World world) {
        super(entityType, world);
    }

    public CursedMeteorEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(ModEntities.CURSED_METEOR, owner, directionX, directionY, directionZ, world);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult) hitResult).getEntity())) {
            if (!this.world.isClient) {
                List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(2.0D, 2.0D, 2.0D));
                if (!list.isEmpty()) {
                    Iterator var5 = list.iterator();
                    while (var5.hasNext()) {
                        LivingEntity livingEntity = (LivingEntity) var5.next();
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 2));
                    }
                }
            }
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), explosionPower, Explosion.DestructionType.NONE);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().damage(DamageSource.magic(this, this.getOwner()), 7.5F);
        super.onEntityHit(entityHitResult);
    }

    @Override
    public void tick() {
        if (isFalling) {
            this.powerY -= 0.03;
        }
        if (this.age >= maximumAgeTicks) {
            this.discard();
        }
        super.tick();
    }

    public void setFalling(boolean shouldFall) {
        isFalling = shouldFall;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SCULK_SOUL;
    }

    protected boolean isBurning() {
        return false;
    }

    public boolean collides() {
        return true;
    }

    public boolean hasNoGravity() {
        return false;
    }
}
