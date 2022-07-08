package net.fenn7.thatchermod.block.entity.custom;

import net.fenn7.thatchermod.sound.ModSounds;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ThatcherEntity extends HostileEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private final ServerBossBar bossBar;
    private double spawnX;
    private double spawnY;
    private double spawnZ;
    private boolean hasSetStartPos;
    private boolean hasReturnedStartPos;
    private boolean isbelow75;
    private boolean isbelow50;
    private boolean isbelow25;
    private boolean hasSummonedSquad;
    private int ticksSinceTracked;
    private int ticksSinceDeath;

    public ThatcherEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.hasSetStartPos = false;
        this.hasReturnedStartPos = false;
        this.isbelow75 = false;
        this.isbelow50 = false;
        this.isbelow25 = false;
        this.hasSummonedSquad = false;
        this.ticksSinceTracked = 0;
        this.ticksSinceDeath = 0;
        this.setPathfindingPenalty(PathNodeType.LAVA, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.experiencePoints = 750;
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D);
    }

    public boolean canBreatheInWater() { return true;}
    public boolean canFreeze() { return false; }
    public boolean isFireImmune() { return true; }
    public boolean isAffectedByDaylight() { return false; }
    public boolean isImmuneToExplosion() { return true; }
    public boolean isUndead() { return true; }
    public boolean cannotDespawn() { return true; }

    protected void initGoals() { // right now has basic AI of a vindicator minus raid mechanics
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
    }

    protected void mobTick() {
            this.world.addParticle(ParticleTypes.LAVA,
                    this.getX(), this.getY() + 1.0D, this.getZ(), 0.0D, 5.0D, 0.0D);
            this.world.addParticle(ParticleTypes.LARGE_SMOKE,
                    this.getX(), this.getY() + 1.0D, this.getZ(), 0.0D, 5.0D, 0.0D);


        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        if (!this.hasSetStartPos) {
            this.spawnX = this.getX();
            this.spawnY = this.getY();
            this.spawnZ = this.getZ();
            this.serverX = this.getX();
            this.serverY = this.getY();
            this.serverZ = this.getZ();
            this.hasSetStartPos = true;
        }

        Entity target = this.getTarget();
        if (target == null) {
            target = getAttacker();
        }
        else if (this.isAlive()) {
            // implements one of 6 special attacks that occurs every 5 seconds on 20TPS against current targets.
            if (this.ticksSinceTracked < 100) {
                this.ticksSinceTracked++;
                if (this.ticksSinceTracked == 100) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 4 + 1);
                    switch (randomNum) {
                        case 0: chainLightningAttack(target); break;
                        case 1: rapidFireAttack(target); break;
                        case 2: explosionAttack(target); break;
                        case 3: disorientAttack(target); break;
                        case 4: gasAttack(target); break;
                    }
                    this.ticksSinceTracked = 0;
                }
            }

            // does a jumpscare attack every interval of 25% health (guaranteed) or randomly per tick
            // different modes should occur as well.
            if (this.getHealth() / this.getMaxHealth() <= 0.75 && !this.isbelow75) {
                jumpScareAttack(target);
                this.isbelow75 = true;
            } else if (this.getHealth() / this.getMaxHealth() <= 0.5 && !this.isbelow50) {
                jumpScareAttack(target);
                isbelow50 = true;
            } else if (this.getHealth() / this.getMaxHealth() <= 0.25 && !this.isbelow25) {
                jumpScareAttack(target);
                isbelow25 = true;
            } else {
                int random = ThreadLocalRandom.current().nextInt(0, 180 + 1);
                if (random == 180) {
                    jumpScareAttack(target);
                }
            }
        }
        if (this.getHealth() / this.getMaxHealth() <= 0.75) {
            this.activateRageMode();
        }
        if (this.getHealth() / this.getMaxHealth() <= 0.5) {
            this.activateDeathSquad();
        }
        if (this.getHealth() / this.getMaxHealth() <= 0.25) {
            this.activateInfernalMode();
        }
    }

    private void chainLightningAttack(Entity target) {
        if (!this.world.isClient()) {
            for (int i = 0; i < 3; i++) {
                EntityType.LIGHTNING_BOLT.spawn((ServerWorld) this.world, null, null, null, target.getSteppingPos(),
                        SpawnReason.TRIGGERED, true, true);
            }
        }
    }

    private void rapidFireAttack(Entity target) {
        for (int i = 0; i < 6; i++) {
            double e = target.getX() - this.getX();
            double f = target.getBodyY(0.5D) - this.getBodyY(0.5D);
            double g = target.getZ() - this.getZ();

            SmallFireballEntity fireBall = new SmallFireballEntity(this.world, this, this.getRandom().nextTriangular(e, 5.0D), f, this.getRandom().nextTriangular(g, 5.0D));
            fireBall.setPosition(fireBall.getX(), this.getBodyY(0.5D) + 0.5D, fireBall.getZ());
            this.world.spawnEntity(fireBall);
        }
    }

    private void explosionAttack(Entity target) {
        this.world.createExplosion(target, target.getX(), target.getY(), target.getZ(), 5, Explosion.DestructionType.NONE);
    }

    private void disorientAttack(Entity target) {
        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100), this);
        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100), this);
    }

    private void gasAttack(Entity target) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.world, target.getX(), target.getY() + 1, target.getZ());
        cloud.setPotion(Potions.STRONG_POISON); cloud.setDuration(100); cloud.setColor(0); cloud.setRadius(3);
        this.world.spawnEntity(cloud);
    }

    private void jumpScareAttack(Entity target) {
         this.setPosition(target.getX(), target.getY(), target.getZ());
         ((LivingEntity) target).setStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1), this);
         ((LivingEntity) target).setStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 9), this);
         world.playSound(null, target.getSteppingPos(), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 10F, 2.0F);
    }

    private void activateRageMode() {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 1));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100));
    }

    private void activateDeathSquad() {
        if (!this.hasSummonedSquad) {
            for (int i = 0; i < 4; i++) {
                BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, this.world);
                switch (i) {
                    case 0: blaze.setPosition(this.getX() + 3, this.getY() + 5, this.getZ()); break;
                    case 1: blaze.setPosition(this.getX(), this.getY() + 5, this.getZ() + 3); break;
                    case 2: blaze.setPosition(this.getX() - 3, this.getY() + 5, this.getZ()); break;
                    case 3: blaze.setPosition(this.getX(), this.getY() + 5, this.getZ() - 3); break;
                }
                world.spawnEntity(blaze);
            }
            this.hasSummonedSquad = true;
        }
    }

    private void activateInfernalMode() {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 2));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 1));

        AreaEffectCloudEntity aura = new AreaEffectCloudEntity(this.world, this.getX(), this.getY() + 1, this.getZ());
        aura.setColor(25500); aura.setRadius(0); aura.setDuration(1);
        this.world.spawnEntity(aura);
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isbelow75 && !(source.getAttacker() instanceof PlayerEntity) && source != DamageSource.OUT_OF_WORLD) {
            // below 75% health, can only be damaged by players OR falling out of the world
            amount = 0.0F;
        }
        else if (this.isbelow25 && source.getAttacker() != null) { //below 25% health, apply a burn effect to attackers
            source.getAttacker().setOnFireFromLava();
        }
        return super.damage(source, amount);
    }

    protected void updatePostDeath() {

        if (!this.hasReturnedStartPos) {
            this.setPosition(this.spawnX, this.spawnY, this.spawnZ);
            this.setPosition(this.serverX, this.serverY, this.serverZ);
            world.playSound(null, this.spawnX, this.spawnY, this.spawnZ, new SoundEvent(new Identifier("thatchermod:thatcher_possession")), SoundCategory.BLOCKS, 7.5F, 0.75F);
            this.hasReturnedStartPos = true;
        }

        this.world.addParticle(ParticleTypes.EXPLOSION,
                this.getX(), this.getY() + 2.0D, this.getZ(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.LAVA,
                this.getX(), this.getY() + 1.0D, this.getZ(), 0.0D, 5.0D, 0.0D);
        this.world.addParticle(ParticleTypes.LARGE_SMOKE,
                this.getX(), this.getY() + 1.0D, this.getZ(), 0.0D, 5.0D, 0.0D);
        if (this.ticksSinceDeath < 101) {
            this.ticksSinceDeath++;
            if (this.ticksSinceDeath == 100) {
                this.emitGameEvent(GameEvent.ENTITY_DIE);
                this.remove(RemovalReason.KILLED);
                this.ticksSinceDeath = 0;
                this.ticksSinceTracked = 0;
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
            if (!target.getWorld().isClient() && this.getHealth() / this.getMaxHealth() <= 0.5) {
                EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, null, null, null, target.getSteppingPos(),
                        SpawnReason.TRIGGERED, true, true);
            }
            if (this.getHealth() / this.getMaxHealth() <= 0.25) {
                damageShield(10);
                target.setPosition(target.getX(), target.getY() + 5, target.getZ());
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
