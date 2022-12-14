package net.fenn7.thatchermod.commonside.entity.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

public abstract class AbstractMilitaryEntity extends PathAwareEntity implements IAnimatable, Angerable {
    protected static final String THIS_OWNER = "Owner";
    protected static final double FOLLOW_RANGE = 25.0D;
    private final AnimationFactory factory = new AnimationFactory(this);
    private int angerTime;
    private int angerPassingCooldown;
    @Nullable
    private UUID angryAt;
    @Nullable
    private MobEntity owner;

    protected AbstractMilitaryEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 15;
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 3.0F);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this, ThatcherEntity.class, AbstractMilitaryEntity.class)).setGroupRevenge());
        this.goalSelector.add(2, new TrackOwnerTargetGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal(this, PlayerEntity.class, 10, false, false,
                e -> this.shouldAngerAt((LivingEntity) e)));
        this.targetSelector.add(3, new ActiveTargetGoal(this, HostileEntity.class, 5, false, false,
                e -> !(e instanceof ThatcherEntity)));
        this.targetSelector.add(4, new UniversalAngerGoal(this, true));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0D, 10));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 10.0F));
    }

    @Override
    public void tick() {
        if (!this.hasAngerTime() && this.getHealth() < this.getMaxHealth()) {
            this.heal(0.05F);
        }
        super.tick();
    }

    @Override
    protected void mobTick() {
        this.tickAngerLogic((ServerWorld) this.world, true);
        if (this.getTarget() != null) {
            this.tickAngerPassing();
        }
        if (this.hasAngerTime()) {
            this.playerHitTimer = this.age;
        }
        super.mobTick();
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
        this.world.getEntitiesByClass(AbstractMilitaryEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR).stream()
                .filter((p) -> p != this)
                .filter((p) -> p.getTarget() == null)
                .filter((p) -> !p.isTeammate(this.getTarget()))
                .forEach((p) -> p.setTarget(this.getTarget())
                );
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
        if (this.owner != null) {
            nbt.putUuid(THIS_OWNER, this.owner.getUuid());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.world, nbt);
        if (!this.world.isClient() && nbt.containsUuid(THIS_OWNER)) {
            UUID ownerID = nbt.getUuid(THIS_OWNER);
            Entity entity = ((ServerWorld) this.world).getEntity(ownerID);
            if (entity instanceof MobEntity mob) {
                this.setOwner(mob);
            }
        }
    }

    @Override
    public void onKilledOther(ServerWorld world, LivingEntity other) {
        if (other instanceof PlayerEntity player) {
            this.forgive(player);
            this.stopAnger();
        }
        super.onKilledOther(world, other);
    }

    public void setOwner(@Nullable MobEntity owner) {
        this.owner = owner;
    }

    public @Nullable MobEntity getOwner() {
        return this.owner;
    }

    public int getAngerTime() {
        return this.angerTime;
    }

    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    public @Nullable UUID getAngryAt() {
        return this.angryAt;
    }

    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    public void chooseRandomAngerTime() {
        this.setAngerTime(100);
    }

    // animations
    @Override
    public abstract void registerControllers(AnimationData animationData);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    // attack its owner's target
    protected class TrackOwnerTargetGoal extends TrackTargetGoal {
        private final TargetPredicate targetPredicate = TargetPredicate.createNonAttackable().ignoreVisibility().ignoreDistanceScalingFactor();
        private final MobEntity owner = AbstractMilitaryEntity.this.owner;

        public TrackOwnerTargetGoal(PathAwareEntity mob) {
            super(mob, false);
        }

        public boolean canStart() {
            return owner != null && owner.getTarget() != null && this.canTrack(owner.getTarget(), this.targetPredicate);
        }

        public void start() {
            AbstractMilitaryEntity.this.setTarget(owner.getTarget());
            super.start();
        }
    }
}
