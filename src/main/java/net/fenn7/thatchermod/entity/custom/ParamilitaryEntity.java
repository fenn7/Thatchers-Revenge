package net.fenn7.thatchermod.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import java.util.UUID;

public class ParamilitaryEntity extends PathAwareEntity implements IAnimatable, Angerable {
    private static final double FOLLOW_RANGE = 32.0D;
    private final AnimationFactory factory = new AnimationFactory(this);
    private final EntityAttributeModifier DOUBLE_SPEED = new EntityAttributeModifier(
            "SPEED_BOOST", 1.0D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    private int attackTicksLeft;
    private int angerTime;
    private int angerPassingCooldown;
    @Nullable private UUID angryAt;
    @Nullable private MobEntity owner;

    public ParamilitaryEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 15;
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 3.0F);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[]{ThatcherEntity.class})).setGroupRevenge(new Class[0]));
        this.goalSelector.add(2, new TrackOwnerTargetGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false,
                e -> this.shouldAngerAt((LivingEntity) e)));
        this.targetSelector.add(3, new ActiveTargetGoal(this, HostileEntity.class, 5, true, false,
                e -> !(e instanceof CreeperEntity || e instanceof ThatcherEntity)));
        this.targetSelector.add(4, new UniversalAngerGoal(this, true));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0D, 10));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 10.0F));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 12.5D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.3D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, FOLLOW_RANGE);
    }

    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.updateEnchantments(random, localDifficulty);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.initEquipment(random, difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.world, nbt);
    }

    protected void mobTick() {
        this.tickAngerLogic((ServerWorld)this.world, true);
        if (this.getTarget() != null) {
            if (this.random.nextInt(100 + 1) == 0 && !world.isClient() && this.distanceTo(this.getTarget()) >= 6.6D) {
                this.jumpAtTarget();
            }
            this.tickAngerPassing();
        }
        if (this.hasAngerTime()) {
            this.playerHitTimer = this.age;
        }
        super.mobTick();
    }

    public void tickMovement() {
        super.tickMovement();
        if (this.attackTicksLeft > 0) {
            --this.attackTicksLeft;
        }
        EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (this.hasAngerTime()) {
            if (this.getTarget() != null && !speed.hasModifier(DOUBLE_SPEED)) {
                speed.addTemporaryModifier(DOUBLE_SPEED);
            }
        } else if (speed.hasModifier(DOUBLE_SPEED)) {
            speed.removeModifier(DOUBLE_SPEED);
        }
    }

    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) return false;
        else {
            this.attackTicksLeft = 10;
            this.world.sendEntityStatus(this, (byte) 4);
            return true;
        }
    }

    public void handleStatus(byte status) { // required to handle attack anim
        if (status == 4) {
            this.attackTicksLeft = 10;
        }
    }

    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        if (other instanceof PlayerEntity player) {
            this.forgive(player);
            this.stopAnger();
        }
        return super.onKilledOther(world, other);
    }

    private void jumpAtTarget() {
        Vec3d current = this.getVelocity();
        Vec3d updated = new Vec3d(this.getTarget().getX() - this.getX(), 0.0D, this.getTarget().getZ() - this.getZ());
        if (updated.lengthSquared() > 1.0E-7D) {
            updated = updated.normalize().multiply(0.4D).add(current.multiply(0.2D));
        }
        this.setVelocity(1.75 * updated.x, 0.5D, 1.75 * updated.z);
    }

    private void tickAngerPassing() {
        if (this.angerPassingCooldown > 0) {
            --this.angerPassingCooldown;
        } else {
            if (this.getVisibilityCache().canSee(this.getTarget()) && this.owner == null) {
                this.world.playSound(null, this.getBlockPos(), SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS,
                        SoundCategory.HOSTILE, 6.6F, 0.66F);
                ParticleEffect anger = ParticleTypes.ANGRY_VILLAGER;
                ((ServerWorld) this.world).spawnParticles(anger, this.getX(), this.getY() + 2.5D, this.getZ(), 3,
                        0, 0, 0, 0);
                this.callBackup();
            }
            this.angerPassingCooldown = 100;
        }
    }

    private void callBackup() {
        double d = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        Box box = Box.from(this.getPos()).expand(d, d * 0.5, d);
        this.world.getEntitiesByClass(ParamilitaryEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR).stream()
                .filter((p) -> p != this)
                .filter((p) -> p.getTarget() == null)
                .filter((p) -> !p.isTeammate(this.getTarget()))
                .forEach((p) -> p.setTarget(this.getTarget())
        );
    }

    public void setOwner(MobEntity owner) {
        this.owner = owner;
    }
    public @Nullable MobEntity getOwner() {
        return owner;
    }

    public int getAttackTicksLeft() { return this.attackTicksLeft; }

    public int getAngerTime() { return this.angerTime; }
    public void setAngerTime(int angerTime) { this.angerTime = angerTime; }
    public @Nullable UUID getAngryAt() { return this.angryAt; }
    public void setAngryAt(@Nullable UUID angryAt) { this.angryAt = angryAt; }
    public void chooseRandomAngerTime() { this.setAngerTime(100); }

    protected SoundEvent getAmbientSound() { return this.hasAngerTime() ? SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS : SoundEvents.BLOCK_NOTE_BLOCK_SNARE; }
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR; }
    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR; }

    //animations
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackTicksLeft() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.paramilitary.attack", false));
        } else if (this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.paramilitary.walk_target", true));
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.paramilitary.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.paramilitary.idle", false));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private class TrackOwnerTargetGoal extends TrackTargetGoal {
        private final TargetPredicate targetPredicate = TargetPredicate.createNonAttackable().ignoreVisibility().ignoreDistanceScalingFactor();
        private MobEntity owner = ParamilitaryEntity.this.owner;

        public TrackOwnerTargetGoal(PathAwareEntity mob) { super(mob, false); }

        public boolean canStart() {
            return owner != null && owner.getTarget() != null && this.canTrack(owner.getTarget(), this.targetPredicate);
        }

        public void start() {
            ParamilitaryEntity.this.setTarget(owner.getTarget());
            super.start();
        }
    }
}
