package net.fenn7.thatchermod.block.entity.custom;

import net.fenn7.thatchermod.block.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class CursedMissileEntity extends ExplosiveProjectileEntity {
    private static float explosionPower = 1.25F;

    public CursedMissileEntity(EntityType<? extends CursedMissileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CursedMissileEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(ModEntities.CURSED_MISSILE, owner, directionX, directionY, directionZ, world);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult)hitResult).getEntity())) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), this.explosionPower, Explosion.DestructionType.NONE);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().setOnFireFromLava();
        if (this.getOwner() != null) { entityHitResult.getEntity().pushAwayFrom(this.getOwner()); }
        super.onEntityHit(entityHitResult);
    }

    @Override
    public void tick() {
        if (this.age >= 200) { this.discard(); }
        super.tick();
    }

    protected ParticleEffect getParticleType() { return ParticleTypes.SOUL_FIRE_FLAME; }
    protected boolean isBurning() { return false; }
    public boolean collides() { return false; }
    public boolean hasNoGravity() { return true; }
}

