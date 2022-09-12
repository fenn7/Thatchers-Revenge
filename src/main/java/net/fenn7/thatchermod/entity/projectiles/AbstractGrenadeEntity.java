package net.fenn7.thatchermod.entity.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AbstractGrenadeEntity extends ThrownItemEntity implements IAnimatable {
    protected final AnimationFactory factory = new AnimationFactory(this);
    protected int maxAgeTicks = 100;
    protected float power;

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
        if (this.age == 1) {
            world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.HOSTILE,
                        1.0F, 1.0F, true);
        }
        super.tick();
    }

    protected void onCollision(HitResult hitResult) {
        if (!this.world.isClient()) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                // testing direction. may be used for bounce physics in future.
                ThatcherMod.LOGGER.warn(((BlockHitResult) hitResult).getSide().asString());
            }
        }
        super.onCollision(hitResult);
    }

    protected abstract void explode(float power);

    public void setMaxAgeTicks(int maxAgeTicks) {
        this.maxAgeTicks = maxAgeTicks;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getPower() { return this.power; }

    protected  <E extends IAnimatable> PlayState flyingAnimation(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.grenade.flying", true));
        return PlayState.CONTINUE;
    }
}
