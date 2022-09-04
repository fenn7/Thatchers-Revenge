package net.fenn7.thatchermod.entity.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public abstract class AbstractGrenadeEntity extends ThrownItemEntity {
    protected int maxAgeTicks;
    protected float explosionPower;

    public AbstractGrenadeEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public AbstractGrenadeEntity(EntityType<? extends ThrownItemEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world);
    }

    public AbstractGrenadeEntity(EntityType<? extends ThrownItemEntity> entityType, World world, double x, double y, double z) {
        super(entityType, x, y, z, world);
    }

    public void tick() {
        if (this.age >= this.maxAgeTicks || this.isOnFire()) {
            explode();
        }
        super.tick();
    }

    protected void onCollision(HitResult hitResult) {
        if (!this.world.isClient()) {
            world.sendEntityStatus(this, (byte) 3);
        }
        super.onCollision(hitResult);
    }

    protected void explode() {
        this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), explosionPower, Explosion.DestructionType.NONE);
        this.discard();
    }

    protected void initAgeAndPower(int maxAgeTicks, float explosionPower) {
        this.maxAgeTicks = maxAgeTicks;
        this.explosionPower = explosionPower;
    }

    public void setMaxAgeTicks(int maxAgeTicks) {
        this.maxAgeTicks = maxAgeTicks;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    public float getExplosionPower() { return this.explosionPower; };
}
