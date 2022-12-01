package net.fenn7.thatchermod.commonside.entity.mobs;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class RoyalFencerEntity extends AbstractMilitaryEntity {
    private final EntityAttributeModifier DOUBLE_SPEED = new EntityAttributeModifier(
            "SPEED_BOOST", 1.0D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public RoyalFencerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, false));
        super.initGoals();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.3D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, FOLLOW_RANGE);
    }

    @Override
    protected void initEquipment(LocalDifficulty localDifficulty) {
        super.initEquipment(localDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.updateEnchantments(localDifficulty);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.initEquipment(difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    protected void mobTick() {
        if (this.random.nextInt(100 + 1) == 0 && !world.isClient() &&
                this.getTarget() != null && this.distanceTo(this.getTarget()) >= 6.6D) {
            this.jumpAtTarget();
        }
        super.mobTick();
    }

    public void tickMovement() {
        EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (this.hasAngerTime()) {
            if (this.getTarget() != null && !speed.hasModifier(DOUBLE_SPEED)) {
                speed.addTemporaryModifier(DOUBLE_SPEED);
            }
        } else if (speed.hasModifier(DOUBLE_SPEED)) {
            speed.removeModifier(DOUBLE_SPEED);
        }
        super.tickMovement();
    }

    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) return false;
        else {
            this.world.sendEntityStatus(this, (byte) 4);
            return true;
        }
    }

    public void handleStatus(byte status) { // required to handle attack anim
        if (status == 4 && this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            this.world.addParticle(ParticleTypes.CRIT,
                    target.getX(), target.getBodyY(0.75D), target.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void jumpAtTarget() {
        Vec3d current = this.getVelocity();
        Vec3d updated = new Vec3d(this.getTarget().getX() - this.getX(), 0.0D, this.getTarget().getZ() - this.getZ());
        if (updated.lengthSquared() > 1.0E-7D) {
            updated = updated.normalize().multiply(0.4D).add(current.multiply(0.2D));
        }
        this.setVelocity(1.75 * updated.x, 0.5D, 1.75 * updated.z);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.hasAngerTime() ? SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS : SoundEvents.BLOCK_NOTE_BLOCK_SNARE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR;
    }

    //animations
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fencer.targeting", true));
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.military.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.military.idle", false));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPred(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fencer.attack", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController(this, "attack", 0, this::attackPred));
    }
}
