package net.fenn7.thatchermod.entity.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractGrenadeEntity extends ThrownItemEntity implements IAnimatable {
    protected final AnimationFactory factory = new AnimationFactory(this);
    protected int maxAgeTicks = 100;
    protected boolean shouldBounce = true;
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
        if (this.age >= this.maxAgeTicks) {
            explode(this.power);
        }
        super.tick();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (this.shouldBounce) {
            String hitSide = blockHitResult.getSide().asString();
            Vec3d velocity = this.getVelocity().multiply(0.65F);

            switch (hitSide) {
                case "up", "down": this.setVelocity(velocity.getX(), -velocity.getY(), velocity.getZ()); break;
                case "east", "west": this.setVelocity(-velocity.getX(), velocity.getY(), velocity.getZ()); break;
                case "north", "south": this.setVelocity(velocity.getX(), velocity.getY(), -velocity.getZ()); break;
            }
        }
        else {
            this.world.sendEntityStatus(this, (byte) 3);
            explode(this.power);
        }
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        this.world.sendEntityStatus(this, (byte) 3);
        entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 1.0F);
        explode(this.power);
        super.onEntityHit(entityHitResult);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("current_age", this.age);
        nbt.putFloat("explosion_power", this.power);
        nbt.putBoolean("should_bounce", this.shouldBounce);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.age = nbt.getInt("current_age");
        this.power = nbt.getFloat("explosion_power");
        this.shouldBounce = nbt.getBoolean("should_bounce");
        super.readNbt(nbt);
    }

    protected abstract void explode(float power);

    public void setMaxAgeTicks(int maxAgeTicks) {
        this.maxAgeTicks = maxAgeTicks;
    }

    public void setShouldBounce(boolean shouldBounce) {
        this.shouldBounce = shouldBounce;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getPower() { return this.power; }

    protected <E extends IAnimatable> PlayState flyingAnimation(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.grenade.flying", true));
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::flyingAnimation));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
