package net.fenn7.thatchermod.commonside.entity.projectiles;

import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

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
        super(ModEntities.SMOKE_GRENADE_ENTITY.get(), world, user);
    }

    public SmokeGrenadeEntity(World world, double x, double y, double z) {
        super(ModEntities.SMOKE_GRENADE_ENTITY.get(), world, x, y, z);
    }

    @Override
    protected void initDataTracker() {
        setPower(SMOKE_RANGE);
        super.initDataTracker();
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.HOSTILE,
                    3.0F, 0.8F, true);
        } else if (status == 33) {
            Box smokeBox = new Box(this.getBlockPos()).expand(this.power, this.power, this.power);
            Stream<BlockPos> posStream = BlockPos.stream(smokeBox);
            posStream.filter(pos -> Math.sqrt(pos.getSquaredDistance(this.getPos())) <= this.power)
                    .filter(pos -> this.world.getBlockState(pos).isAir())
                    .forEach(pos -> {
                        ParticleEffect smoke1 = ParticleTypes.CAMPFIRE_COSY_SMOKE;
                        ParticleEffect smoke2 = ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
                        double x = ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D);
                        double z = ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D);
                        this.world.addParticle(smoke1, pos.getX() - x, pos.getY() + 0.4, pos.getZ() - z, 0, 0.01, 0);
                        this.world.addParticle(smoke2, pos.getX() + x, pos.getY() - 0.4, pos.getZ() + z, 0, 0.01, 0);
                    });

            List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, smokeBox);
            list.stream().filter(e -> Math.sqrt(e.squaredDistanceTo(this.getX(), this.getY(), this.getZ())) <= this.power)
                    .forEach(e -> {
                        if (e.isOnFire()) {
                            e.setOnFire(false);
                        }
                    });
        } else {
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
            if (this.smokeTicks >= 0 && this.smokeTicks % 5 == 0) {
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
        this.setVelocity(Vec3d.ZERO);
        this.setNoGravity(true);
        this.shouldSmoke = true;

        BlockPos impactPos = this.getBlockPos();
        Box impactBox = new Box(impactPos).expand(power, power, power);

        Stream<BlockPos> posStream = BlockPos.stream(impactBox);
        posStream.filter(pos -> Math.sqrt(pos.getSquaredDistance(impactPos)) <= power)
                .forEach(pos -> {
                    BlockState blockState = this.world.getBlockState(pos);
                    if (blockState.getProperties().contains(Properties.LIT)) {
                        world.setBlockState(pos, blockState.with(Properties.LIT, false), 11);
                    } else if (this.world.getBlockState(pos).isIn(BlockTags.FIRE)) {
                        world.removeBlock(pos, false);
                    }
                });
    }

    public boolean isSmoking() {
        return this.shouldSmoke;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GRENADE_SMOKE.get();
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
}
