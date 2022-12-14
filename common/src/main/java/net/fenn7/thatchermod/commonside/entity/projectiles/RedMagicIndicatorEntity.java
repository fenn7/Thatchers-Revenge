package net.fenn7.thatchermod.commonside.entity.projectiles;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.util.CommonMethods;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class RedMagicIndicatorEntity extends ExplosiveProjectileEntity {
    private int degrees = 0;
    private int maxAge = -1;
    private boolean shouldInvertRadius = false;
    private BlockPos startPoint = null;
    private BlockPos centrePoint = null;

    public RedMagicIndicatorEntity(EntityType<? extends RedMagicIndicatorEntity> entityType, World world) {
        super(entityType, world);
    }

    public RedMagicIndicatorEntity(World world, double x, double y, double z, boolean reverseRadius) {
        super(ModEntities.RED_MAGIC_ENTITY.get(), x, y, z, 0, 0, 0, world);
        this.shouldInvertRadius = reverseRadius;
        this.startPoint = new BlockPos(x, y, z);
    }

    public void setCentrePoint(BlockPos centrePos) {
        this.centrePoint = centrePos;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void tick() {
        if (this.age != -1 && this.age >= this.maxAge) {
            this.discard();
        }
        if (this.centrePoint != null) {
            double radius = 2 * Math.sqrt(this.centrePoint.getSquaredDistance(this.startPoint)); // adjust this value to control the size of the circle
            if (this.shouldInvertRadius) radius *= -1;
            double x = radius * Math.cos(Math.toRadians(degrees));
            double z = radius * Math.sin(Math.toRadians(degrees));

            // set the position of the particle to the calculated x and y values
            this.setPos(this.startPoint.getX() + x, this.getY(), this.startPoint.getZ() + z); // add a fixed position on top of this

            this.degrees += 12;
            if (this.degrees == 360) {
                this.degrees = 12;
            }
        }
        if (this.age % 5 == 0) {
            DustParticleEffect redDust = new DustParticleEffect(new Vec3f(1, 0, 0), 10);
            this.world.addParticle(redDust, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        super.tick();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("invert.radius", this.shouldInvertRadius);
        nbt.putInt("max.age", this.maxAge);
        nbt.putDouble("startX", this.startPoint.getX());
        nbt.putDouble("startY", this.startPoint.getY());
        nbt.putDouble("startZ", this.startPoint.getZ());
        nbt.putDouble("centreX", this.centrePoint.getX());
        nbt.putDouble("centreY", this.centrePoint.getY());
        nbt.putDouble("centreZ", this.centrePoint.getZ());
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.shouldInvertRadius = nbt.getBoolean("invert.radius");
        this.maxAge = nbt.getInt("max.age");
        this.startPoint = new BlockPos(nbt.getDouble("startX"), nbt.getDouble("startY"), nbt.getDouble("startZ"));
        this.centrePoint = new BlockPos(nbt.getDouble("centreX"), nbt.getDouble("centreY"), nbt.getDouble("centreZ"));
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.LARGE_SMOKE;
    }

    public boolean collides() {
        return false;
    }

    public boolean hasNoGravity() {
        return true;
    }
}
