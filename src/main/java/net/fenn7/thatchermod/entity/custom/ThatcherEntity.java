package net.fenn7.thatchermod.entity.custom;

import net.minecraft.block.BlockState;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.concurrent.ThreadLocalRandom;

public class ThatcherEntity extends HostileEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private final ServerBossBar bossBar;
    private boolean hasSetStartPos;
    private boolean hasReturnedStartPos;
    private boolean isbelow75;
    private boolean isbelow50;
    private boolean isbelow25;
    private boolean hasSummonedSquad;
    private int ticksSinceTracked;
    private int ticksSinceDeath;
    private int attackTicksLeft;

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
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.experiencePoints = 750;
        this.bossBar = (ServerBossBar)(new ServerBossBar(Text.literal("§1" + this.getDisplayName().getString()), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("isbelow75", this.isbelow75);
        nbt.putBoolean("isbelow50", this.isbelow50);
        nbt.putBoolean("isbelow25", this.isbelow25);
        nbt.putBoolean("summoned", this.hasSummonedSquad);
        nbt.putBoolean("setStartPos", this.hasSetStartPos);
        nbt.putBoolean("returned", this.hasReturnedStartPos);
        nbt.putInt("since.tracked", this.ticksSinceTracked);
        nbt.putInt("since.death", this.ticksSinceDeath);
        nbt.putDouble("startX", this.serverX);
        nbt.putDouble("startY", this.serverY);
        nbt.putDouble("startZ", this.serverZ);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.isbelow75 = nbt.getBoolean("isbelow75");
        this.isbelow50 = nbt.getBoolean("isbelow50");
        this.isbelow25 = nbt.getBoolean("isbelow25");
        this.hasSummonedSquad = nbt.getBoolean("summoned");
        this.hasSetStartPos = nbt.getBoolean("setStartPos");
        this.hasReturnedStartPos = nbt.getBoolean("returned");
        this.ticksSinceTracked = nbt.getInt("since.tracked");
        this.ticksSinceDeath = nbt.getInt("since.death");
        this.serverX = nbt.getDouble("startX");
        this.serverY = nbt.getDouble("startY");
        this.serverZ = nbt.getDouble("startZ");
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 450.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0D);
    }

    public boolean canBreatheInWater() { return true;}
    public boolean canFreeze() { return false; }
    public boolean isFireImmune() { return true; }
    public boolean isAffectedByDaylight() { return false; }
    public boolean isImmuneToExplosion() { return true; }
    public boolean isUndead() { return true; }
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) { return false; }
    public EntityGroup getGroup() { return EntityGroup.UNDEAD; }

    public boolean cannotDespawn() {
        return !(this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful());
    }

    protected void initGoals() { // right now has basic melee AI
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal(this, MobEntity.class, false,
                (entity) -> entity instanceof MobEntity && !(entity instanceof ParamilitaryEntity) && !(((MobEntity) entity).isUndead())));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
    }

    public void endCombat() {
        this.ticksSinceTracked = 0;
        super.endCombat();
    }

    public void tickMovement() {
        super.tickMovement();
        if (this.attackTicksLeft > 0) {
            --this.attackTicksLeft;
        }
    }

    protected void mobTick() {
        float healthRatio = this.getHealth()/this.getMaxHealth();
        this.bossBar.setPercent(healthRatio);
        if (!this.hasSetStartPos) {
            this.serverX = this.getX();
            this.serverY = this.getY();
            this.serverZ = this.getZ();
            this.hasSetStartPos = true;
        }
        LivingEntity target = this.getTarget();
        if (this.isAlive() && target != null && target.isAttackable()) {
            // implements one of 6 special attacks that occurs every 5 seconds on 20TPS against current targets.
            if (this.ticksSinceTracked < 100) {
                this.ticksSinceTracked++;
                if (this.ticksSinceTracked == 100) {
                    this.world.sendEntityStatus(this, (byte) 60); // adds "poof" indication of attack used
                    this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.HOSTILE, 1.5F, 2.0F);
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
                    switch (randomNum) {
                        case 0: chainLightningAttack(target); break;
                        case 1: rapidFireAttack(target); break;
                        case 2: gasAttack(target); break;
                        case 3: meteorAttack(target); break;
                    }
                    this.ticksSinceTracked = 0;
                }
            }
            // does a jumpscare attack every interval of 25% health (guaranteed) or randomly per tick
            if (healthRatio <= 0.75 && !this.isbelow75) {
                jumpScareAttack(target);
                this.isbelow75 = true;
            } else if (healthRatio <= 0.5 && !this.isbelow50) {
                jumpScareAttack(target);
                this.isbelow50 = true;
            } else if (healthRatio <= 0.25 && !this.isbelow25) {
                jumpScareAttack(target);
                this.isbelow25 = true;
            } else {
                int random = ThreadLocalRandom.current().nextInt(0, 200 + 1);
                if (random == 200) {
                    jumpScareAttack(target);
                }
            }
        }
        // different modes should occur based on health.
        if (healthRatio <= 0.75 && healthRatio > 0.5) {
            this.activateRageMode();
        }
        else if (healthRatio <= 0.5 && healthRatio > 0.25) {
            this.activateDeathSquad();
        }
        else if (healthRatio <= 0.25) {
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
            double f = target.getBodyY(0.5D) - (this.getBodyY(0.5D) + 0.5D);
            double g = target.getZ() - this.getZ();

            CursedMissileEntity fireBall = new CursedMissileEntity(this.world, this, this.getRandom().nextTriangular(e, 2.5D), f, this.getRandom().nextTriangular(g, 2.5D));
            fireBall.setPosition(fireBall.getX() + ThreadLocalRandom.current().nextDouble(-1, 1 + 1), this.getBodyY(0.5D) + 0.5D, fireBall.getZ() + ThreadLocalRandom.current().nextDouble(-1, 1 + 1));
            this.world.spawnEntity(fireBall);
        }
    }

    private void gasAttack(Entity target) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.world, target.getX(), target.getY() + 1, target.getZ());
        cloud.setPotion(Potions.HARMING); cloud.setDuration(200); cloud.setColor(0); cloud.setRadius(4);
        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 1), this);
        this.world.spawnEntity(cloud);
    }

    private void meteorAttack(Entity target) {
        CursedMeteorEntity meteor = new CursedMeteorEntity(this.world, this, 0, 0, 0);
        meteor.setPosition(target.getX(), target.getY() + 15, target.getZ());
        meteor.setFalling(true);
        world.spawnEntity(meteor);
    }

    private void jumpScareAttack(Entity target) {
         this.setPosition(target.getX(), target.getY(), target.getZ());
         ((LivingEntity) target).setStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1), this);
         ((LivingEntity) target).setStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 9), this);
         world.playSound(null, target.getSteppingPos(), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 12.5F, 2.0F);
    }

    private void activateRageMode() {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100));
    }

    private void activateDeathSquad() {
        if (!this.hasSummonedSquad) {
            for (int i = 0; i < 4; i++) {
                SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, this.world);
                EquipmentSlot[] slots = EquipmentSlot.values(); // equips armour
                for (EquipmentSlot slot : slots) {
                    skeleton.equipStack(slot, new ItemStack(MobEntity.getEquipmentForSlot(slot, 3)));
                }
                skeleton.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                if (i < 2) { skeleton.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE)); }
                else { skeleton.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW)); }

                skeleton.setPosition(this.getX(), this.getY(), this.getZ());
                world.spawnEntity(skeleton);
            }
            this.hasSummonedSquad = true;
        }
    }

    private void activateInfernalMode() {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 1));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 1));
        this.world.sendEntityStatus(this, (byte) 63);
    }

    public boolean damage(DamageSource source, float amount) {
        float healthRatio = this.getHealth() / this.getMaxHealth();
        if ((healthRatio <= 0.75 && !(source.getAttacker() instanceof PlayerEntity)
                && source != DamageSource.OUT_OF_WORLD) || (source == DamageSource.LIGHTNING_BOLT)) {
            // below 75% health, can only be damaged by players OR falling out of the world. Immune to lightning.
            amount = 0.0F;
        }
        if (healthRatio <= 0.25 && source.getAttacker() != null) {
            //below 25% health, apply a burn effect to attackers.
            source.getAttacker().setOnFireFromLava();
        }
        return super.damage(source, amount);
    }

    public void handleStatus(byte status) { // required to handle particles
        if (status == 4) {
            this.attackTicksLeft = 10;
            this.playSound(SoundEvents.AMBIENT_CAVE, 5.0F, 2.0F);
        }
        else if (status == 63) {
            this.world.addParticle(ParticleTypes.LAVA,
                    this.getX(), this.getY() + 1.0D, this.getZ(), 0.0D, 5.0D, 0.0D);
            this.world.addParticle(ParticleTypes.LARGE_SMOKE,
                    this.getX(), this.getY(), this.getZ(), 0.0D, 0.1D, 0.0D);
            this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.BLOCKS,
                    5F, 0.5F);
        }
        else {
            super.handleStatus(status);
        }
    }

    public void onDeath(DamageSource damageSource) {
        world.playSound(null, this.getBlockPos(), new SoundEvent(new Identifier("thatchermod:thatcher_possession")), SoundCategory.BLOCKS, 5F, 0.75F);
        if (!this.hasReturnedStartPos) {
            BlockPos pos = new BlockPos(this.serverX, this.serverY, this.serverZ);
            this.setPosition(Vec3d.ofCenter(pos));

            BlockState blockState = world.getBlockState(pos);
            if (blockState.isIn(BlockTags.FIRE)) {
                world.removeBlock(pos, false);
            }
            this.hasReturnedStartPos = true;
        }
        super.onDeath(damageSource);
    }

    protected void updatePostDeath() {
        this.world.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 2.0D, this.getZ(), 0.0D, 0.0D, 0.0D);
        if (this.ticksSinceDeath < 100) {
            this.ticksSinceDeath++;
            if (this.ticksSinceDeath == 100) {
                this.emitGameEvent(GameEvent.ENTITY_DIE);
                this.remove(RemovalReason.KILLED);
                this.ticksSinceDeath = 0;
                this.ticksSinceTracked = 0;
            }
        }
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
        if (!super.tryAttack(target)) return false;
        else {
            this.attackTicksLeft = 10;
            this.world.sendEntityStatus(this, (byte) 4);
            float healthRatio = this.getHealth() / this.getMaxHealth();
            if (!target.getWorld().isClient() && healthRatio <= 0.25) {
                damageShield(10);
                EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, null, null, null, target.getSteppingPos(),
                        SpawnReason.TRIGGERED, true, true);
                double x = target.getX() - this.getX();
                double z = target.getZ() - this.getZ();
                if (target instanceof LivingEntity living) {
                    living.takeKnockback(2.0D, x, z);
                }
            }
        }
        return true;
    }

    public int getAttackTicksLeft() {
        return this.attackTicksLeft;
    }

    // animations and sounds
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isAlive()) {
            if (this.isAttacking() && this.getAttackTicksLeft() > 0) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thatcher.attack_2", false));
            } else if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thatcher.move", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thatcher.idle", true));
            }
            return PlayState.CONTINUE;
        }
        else return PlayState.STOP;
    }

    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    public AnimationFactory getFactory() {
        return factory;
    }

    protected void onBlockCollision(BlockState state) { if (state.getBlock().getHardness() >= 0) { /* add breaking later */ } super.onBlockCollision(state); }
    protected SoundEvent getAmbientSound() { return SoundEvents.AMBIENT_NETHER_WASTES_MOOD; }
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK; }
    protected void playStepSound(BlockPos pos, BlockState state) { this.playSound(SoundEvents.BLOCK_NETHERITE_BLOCK_BREAK, 0.5f, 1.0f); }
}
