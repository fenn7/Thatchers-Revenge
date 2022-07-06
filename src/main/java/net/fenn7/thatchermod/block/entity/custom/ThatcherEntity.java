package net.fenn7.thatchermod.block.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ThatcherEntity extends HostileEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private final ServerBossBar bossBar;
    private int ticksSinceTracked = 0;
    private int ticksSinceDeath = 0;

    public ThatcherEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.experiencePoints = 6900;
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f);
    }

    public boolean isOnFire() {
        return false;
    }

    protected void initGoals() { // right now has basic AI of a vindicator minus raid mechanics
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
    }

    protected void mobTick() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        if (ticksSinceTracked < 121) {
            ticksSinceTracked++;
            if (ticksSinceTracked == 120) {
                Entity target = this.getTarget();
                if (target instanceof ServerPlayerEntity) {
                    this.setPosition(target.getX(), target.getY(), target.getZ());
                    ((ServerPlayerEntity) target).setStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1), this);
                    ((ServerPlayerEntity) target).setStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 9), this);
                    ((ServerPlayerEntity) target).playSound(SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 10F, 2.0F);
                }
                ticksSinceTracked = 0;
            }
        }
    }

    protected void updatePostDeath() {
        this.world.addParticle(ParticleTypes.EXPLOSION,
                this.getX(), this.getY() + 2.0D, this.getZ(), 0.0D, 0.0D, 0.0D);
        if (ticksSinceDeath < 101) {
            ticksSinceDeath++;
            if (ticksSinceDeath == 100) {
                this.emitGameEvent(GameEvent.ENTITY_DIE);
                this.remove(RemovalReason.KILLED);
                ticksSinceDeath = 0;
                ticksSinceTracked = 0;
            }
        }
    }

    public void onSummoned() {
        this.bossBar.setPercent(0.0F);
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) {
            return false;
        } else {
            if (!target.getWorld().isClient()){
                EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, null, null, null, target.getSteppingPos(),
                        SpawnReason.TRIGGERED, true, true);
            }
            if (target instanceof LivingEntity) {
                ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 140), this);
                ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20), this);
                this.heal(0F);
            }
        }
        return true;
    }


    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    // animations (TBA)
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.raccoon.walk", true));
            return PlayState.CONTINUE;
        }

        //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.raccoon.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
