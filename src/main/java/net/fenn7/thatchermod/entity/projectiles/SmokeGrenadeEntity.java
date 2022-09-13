package net.fenn7.thatchermod.entity.projectiles;

import com.eliotlash.mclib.math.functions.classic.Mod;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class SmokeGrenadeEntity extends AbstractGrenadeEntity implements IAnimatable {
    private static final float SMOKE_RANGE = 2.8F;
    private boolean shouldSmoke = false;
    private int smokeTicks = 0;

    public SmokeGrenadeEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public SmokeGrenadeEntity(World world, PlayerEntity user) {
        super(ModEntities.SMOKE_GRENADE_ENTITY, world, user);
    }

    public SmokeGrenadeEntity(World world, double x, double y, double z) {
        super(ModEntities.SMOKE_GRENADE_ENTITY, world, x, y, z);
    }

    @Override
    protected void initDataTracker() {
        setPower(SMOKE_RANGE);
        super.initDataTracker();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.world.isClient()) {
            this.world.sendEntityStatus(this, (byte) 3);
            this.setVelocity(Vec3d.ZERO);
            this.setNoGravity(true);
            this.shouldSmoke = true;
            explode(this.power);
        }
        super.onCollision(hitResult);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.HOSTILE,
                    3.0F, 0.8F, true);
        }
        else if (status == 33) {
            ParticleEffect effect = ParticleTypes.CAMPFIRE_COSY_SMOKE;
            Box smokeBox = new Box(this.getBlockPos()).expand(this.power, this.power, this.power);

            Stream<BlockPos> posStream = BlockPos.stream(smokeBox);
            posStream.filter(pos -> Math.sqrt(pos.getSquaredDistance(this.getBlockPos())) <= this.power)
                    .forEach(pos -> {
                        double x = ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D);
                        double z = ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D);
                        BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                        CampfireBlock.spawnSmokeParticle(this.world, newPos, true, true);
                    });
        }
        else {
            super.handleStatus(status);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return source.isOutOfWorld();
    }

    @Override
    public void tick() {
        if (this.shouldSmoke) {
            this.setVelocity(Vec3d.ZERO);
            if (this.smokeTicks >= 0 && this.smokeTicks % 15 == 0) {
                this.world.sendEntityStatus(this, (byte) 33);
            }
            this.smokeTicks++;
            if (this.smokeTicks == 200) {
                this.discard();
            }
        }
        super.tick();
    }

    @Override
    protected void explode(float power) {
        BlockPos impactPos = this.getBlockPos();
        Box impactBox = new Box(impactPos).expand(power, power, power);

        Stream<BlockPos> posStream = BlockPos.stream(impactBox);
        posStream.filter(pos -> Math.sqrt(pos.getSquaredDistance(impactPos)) <= power)
                .filter(pos -> this.world.getBlockState(pos).isIn(BlockTags.FIRE))
                .forEach(pos -> world.removeBlock(pos, false));

        List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, impactBox);
        list.stream().filter(e -> Math.sqrt(e.squaredDistanceTo(
                        impactPos.getX(), impactPos.getY(), impactPos.getZ())) <= power)
                .forEach(e -> e.setStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 120, 0, true, false), this));
    }

    public boolean isSmoking() {
        return this.shouldSmoke;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GRENADE_SMOKE;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("shouldSmoke", this.shouldSmoke);
        nbt.putInt("smokeTicks", this.smokeTicks);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.shouldSmoke = nbt.getBoolean("shouldSmoke");
        this.smokeTicks = nbt.getInt("smokeTicks");
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::flyingAnimation));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
