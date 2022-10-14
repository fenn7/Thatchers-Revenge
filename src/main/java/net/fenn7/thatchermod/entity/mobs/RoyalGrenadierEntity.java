package net.fenn7.thatchermod.entity.mobs;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.checkerframework.checker.units.qual.A;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

public class RoyalGrenadierEntity extends AbstractMilitaryEntity {
    private final AnimationFactory factory = new AnimationFactory(this);
    public RoyalGrenadierEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new GrenadeAttackGoal(this, 1.0D, 30, 24.0F));
        this.goalSelector.add(0, new ActiveTargetGoal(this, CowEntity.class, false));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 12.5D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.3D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24);
    }

    public void attack(LivingEntity target) {
        GrenadeEntity grenade = new GrenadeEntity(this.world, this.getX(), this.getBodyY(0.7F), this.getZ());
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.1D) - grenade.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        grenade.setVelocity(d, e + g * 0.2D, f, 1.0F, ThreadLocalRandom.current().nextFloat(1.0F, 3.0F));
        this.world.spawnEntity(grenade);
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private static class GrenadeAttackGoal extends Goal {
        private final RoyalGrenadierEntity grenadier;
        private final double speed;
        private int attackInterval;
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

        public void setAttackInterval(int attackInterval) {
            this.attackInterval = attackInterval;
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
                    //int i = this.grenadier.getItemUseTime();
                    if (this.cooldown <= 0) {
                        this.grenadier.attack(target);
                        this.cooldown = this.attackInterval;
                    }
                }
            } /*else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
                    this.grenadier.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.grenadier, Items.BOW));
                }*/
        }
    }
}

