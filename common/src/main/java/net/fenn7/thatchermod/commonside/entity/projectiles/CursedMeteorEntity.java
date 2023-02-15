package net.fenn7.thatchermod.commonside.entity.projectiles;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Iterator;
import java.util.List;

public class CursedMeteorEntity extends ExplosiveProjectileEntity {
    private static final float explosionPower = 2.5F;
    private static final int maximumAgeTicks = 300;
    private boolean isFalling = false;
    private boolean isMobSpawned = false;
    private double lowestNoClipY = this.world.getBottomY();

    public CursedMeteorEntity(EntityType<? extends CursedMeteorEntity> entityType, World world) {
        super(entityType, world);
    }

    public CursedMeteorEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(ModEntities.CURSED_METEOR.get(), owner, directionX, directionY, directionZ, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (this.getY() > this.lowestNoClipY) {
            return;
        }
        super.onCollision(hitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().damage(DamageSource.magic(this, this.getOwner()), 2.5F);
    }

    private void explode() {
        if (!this.world.isClient) {
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(1.5D, 1.5D, 1.5D));
            if (!list.isEmpty()) {
                Iterator<LivingEntity> var5 = list.iterator();
                while (var5.hasNext()) {
                    LivingEntity livingEntity = var5.next();
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 2));
                    livingEntity.damage(DamageSource.magic(this, this.getOwner()), 7.5F);
                }
            }
        }
        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && this.isMobSpawned) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), explosionPower, Explosion.DestructionType.NONE);
        } else {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), explosionPower, true, Explosion.DestructionType.DESTROY);
        }
        this.discard();
    }

    @Override
    public void tick() {
        this.noClip = this.getY() > this.lowestNoClipY;
        if (!this.noClip) {
            this.explode();
        }
        if (this.isFalling && this.powerY >= -0.4) {
            this.powerY -= 0.02;
        }
        if (this.age >= maximumAgeTicks) {
            this.discard();
        }
        super.tick();
    }

    @Override
    public boolean isCollidable() {
        return this.getY() <= this.lowestNoClipY;
    }

    public void setLowestNoClipY(double lowestY) { this.lowestNoClipY = lowestY; }

    public void setFalling(boolean shouldFall) {
        this.isFalling = shouldFall;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SOUL;
    }

    protected boolean isBurning() {
        return false;
    }

    public boolean collides() {
        return !this.noClip;
    }

    public boolean hasNoGravity() {
        return false;
    }

    public void setMobSpawned(boolean isMobSpawned) { this.isMobSpawned = isMobSpawned; }

    public boolean isMobSpawned() { return this.isMobSpawned; }
}
