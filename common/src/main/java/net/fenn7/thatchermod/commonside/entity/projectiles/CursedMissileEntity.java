package net.fenn7.thatchermod.commonside.entity.projectiles;

import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class CursedMissileEntity extends ExplosiveProjectileEntity {
    private static final float explosionPower = 1.25F;
    private static final int maximumAgeTicks = 200;

    public CursedMissileEntity(EntityType<? extends CursedMissileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CursedMissileEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(ModEntities.CURSED_MISSILE.get(), owner, directionX, directionY, directionZ, world);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult) hitResult).getEntity())) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), explosionPower, Explosion.DestructionType.NONE);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().setOnFireFromLava();
        entityHitResult.getEntity().damage(DamageSource.magic(this, this.getOwner()), 5.0F);
        super.onEntityHit(entityHitResult);
    }

    @Override
    public void tick() {
        if (this.age >= maximumAgeTicks) {
            this.discard();
        }
        super.tick();
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }

    protected boolean isBurning() {
        return false;
    }

    public boolean collides() {
        return false;
    }

    public boolean hasNoGravity() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }
}

