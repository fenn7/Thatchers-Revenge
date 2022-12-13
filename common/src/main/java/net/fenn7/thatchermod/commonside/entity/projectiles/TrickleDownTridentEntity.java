package net.fenn7.thatchermod.commonside.entity.projectiles;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.List;

public class TrickleDownTridentEntity extends TridentEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private final List<LivingEntity> previousTargets = new ArrayList<>();
    private float bounceRange = 8.0F;
    private boolean shouldCollide = true;

    public TrickleDownTridentEntity(EntityType<? extends TrickleDownTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public TrickleDownTridentEntity(World world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        BlockPos targetPos = entityHitResult.getEntity().getBlockPos();
        Box rangeBox = new Box(targetPos).expand(this.bounceRange);
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity entity : world.getOtherEntities(entityHitResult.getEntity(), rangeBox)) {
            if (entity != this.getOwner() && entity instanceof LivingEntity alive && alive.canSee(this) && !this.previousTargets.contains(entity) &&
                    Math.sqrt(entity.squaredDistanceTo(targetPos.getX(), targetPos.getY(), targetPos.getZ())) <= this.bounceRange) {
                targets.add(alive);
            }
        }
        LivingEntity nearestTarget = world.getClosestEntity(targets, TargetPredicate.createAttackable(),
                (LivingEntity) this.getOwner(), targetPos.getX(), targetPos.getY(), targetPos.getZ());
        if (nearestTarget != null) {
            double d = nearestTarget.getX() - this.getX();
            double e = nearestTarget.getBodyY(0.5D) - this.getY();
            double f = nearestTarget.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            this.setVelocity(d, e + g * 0.2D, f, 1.0F, 0.0F);
            this.playSound(SoundEvents.BLOCK_CHAIN_BREAK, 25.0F, 0.75F);
            this.bounceRange /= 2;
            this.previousTargets.add(nearestTarget);
        } else {
            this.previousTargets.clear();
            this.bounceRange = 8.0F;
            this.shouldCollide = false;
        }
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.shouldCollide ? ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit) : null;
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.bounceRange = nbt.getFloat("bounce.range");
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("bounce.range", this.bounceRange);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.TRICKLE_DOWN_TRIDENT_ENTITY.get();
    }

    private <E extends IAnimatable> PlayState spinAnimation(AnimationEvent<E> event) {
        if (!this.inGround) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trickle_down_trident.spin", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trickle_down_trident.stuck", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::spinAnimation));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}