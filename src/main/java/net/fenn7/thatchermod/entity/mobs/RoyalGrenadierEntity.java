package net.fenn7.thatchermod.entity.mobs;

import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.SmokeGrenadeEntity;
import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
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

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

public class RoyalGrenadierEntity extends AbstractMilitaryEntity {
    private boolean hasUsedSmoke = false;

    public RoyalGrenadierEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new GrenadeAttackGoal(this, 1.0D, 30, (float) FOLLOW_RANGE));
        super.initGoals();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, FOLLOW_RANGE);
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GRENADE_LAUNCHER));
        this.updateEnchantments(random, localDifficulty);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.initEquipment(random, difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public void attack(LivingEntity target) {
        this.handSwinging = true;
        AbstractGrenadeEntity grenade;

        if (!this.hasUsedSmoke) {
            grenade = new SmokeGrenadeEntity(this.world, this.getX(), this.getBodyY(0.7F), this.getZ());
            this.hasUsedSmoke = true;
        } else {
            grenade = new GrenadeEntity(this.world, this.getX(), this.getBodyY(0.7F), this.getZ());
            grenade.setPower(1.5F * grenade.getPower());
        }

        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.2D) - grenade.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);

        grenade.setVelocity(d, e + g * 0.2D, f, 1.4F, ThreadLocalRandom.current().nextFloat(0.0F, 1.0F));
        grenade.setShouldBounce(false);
        grenade.setOwner(this);
        this.world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.HOSTILE, 1.4F, 0.5F);
        this.world.spawnEntity(grenade);
    }

    public boolean hasUsedSmoke() {
        return this.hasUsedSmoke;
    }

    @Override
    public void endCombat() {
        super.endCombat();
        this.hasUsedSmoke = false;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        GrenadeEntity grenade = new GrenadeEntity(this.world, this.getX(), this.getBodyY(0.5), this.getZ());
        grenade.setPower(grenade.getPower() * 2.0F);
        grenade.setShouldBounce(false);
        this.world.spawnEntity(grenade);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.hasAngerTime() ? SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS : SoundEvents.BLOCK_NOTE_BLOCK_GUITAR;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ITEM_SHIELD_BREAK;
    }

    // animations
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.grenadier.targeting", true));
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.grenadier.attack", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController(this, "attack", 0, this::attackPred));
    }

    // shooting
    private static class GrenadeAttackGoal extends Goal {
        private final RoyalGrenadierEntity grenadier;
        private final double speed;
        private final int attackInterval;
        private final float squaredRange;
        private int cooldown = -1;
        private int targetSeeingTicker;
        private boolean movingToLeft;
        private boolean backward;
        private int combatTicks = -1;

        public GrenadeAttackGoal(RoyalGrenadierEntity grenadier, double speed, int attackInterval, float range) {
            this.grenadier = grenadier;
            this.speed = speed;
            this.attackInterval = attackInterval;
            this.squaredRange = range * range;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            return this.grenadier.getTarget() != null;
        }

        public boolean shouldContinue() {
            return (this.canStart() || !this.grenadier.getNavigation().isIdle());
        }

        public void start() {
            super.start();
            this.grenadier.setAttacking(true);
            this.cooldown = this.attackInterval;
        }

        public void stop() {
            super.stop();
            this.grenadier.setAttacking(false);
            this.targetSeeingTicker = 0;
            this.cooldown = -1;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.grenadier.getTarget();
            if (target != null) {
                double distance = this.grenadier.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
                boolean canSee = this.grenadier.getVisibilityCache().canSee(target);
                boolean isTickingSight = this.targetSeeingTicker > 0;
                if (canSee != isTickingSight) {
                    this.targetSeeingTicker = 0;
                }

                if (canSee) {
                    ++this.targetSeeingTicker;
                } else {
                    --this.targetSeeingTicker;
                }

                if (!(distance > this.squaredRange) && this.targetSeeingTicker >= 20) {
                    this.grenadier.getNavigation().stop();
                    ++this.combatTicks;
                } else {
                    this.grenadier.getNavigation().startMovingTo(target, this.speed);
                    this.combatTicks = -1;
                }

                if (this.combatTicks >= 20) {
                    if (this.grenadier.getRandom().nextFloat() < 0.3D) {
                        this.movingToLeft = !this.movingToLeft;
                    }

                    if (this.grenadier.getRandom().nextFloat() < 0.3D) {
                        this.backward = !this.backward;
                    }

                    this.combatTicks = 0;
                }

                if (this.combatTicks >= 0) {
                    if (distance > (this.squaredRange * 0.75F)) {
                        this.backward = false;
                    } else if (distance < (this.squaredRange * 0.25F)) {
                        this.backward = true;
                    }

                    this.grenadier.getMoveControl().strafeTo(this.backward ? -0.75F : 0.75F, this.movingToLeft ? 0.75F : -0.75F);
                    this.grenadier.lookAtEntity(target, 30.0F, 30.0F);
                } else {
                    this.grenadier.getLookControl().lookAt(target, 30.0F, 30.0F);
                }

                if (canSee) {
                    --this.cooldown;
                    if (this.cooldown <= 0) {
                        this.grenadier.attack(target);
                        this.cooldown = this.attackInterval;
                    }
                }
            }
        }
    }
}

