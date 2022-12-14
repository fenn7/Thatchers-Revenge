package net.fenn7.thatchermod.commonside.entity.projectiles;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.util.CommonMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class RedMagicIndicatorEntity extends ExplosiveProjectileEntity {
    private int degrees = 0;
    public RedMagicIndicatorEntity(EntityType<? extends RedMagicIndicatorEntity> entityType, World world) {
        super(entityType, world);
    }

    public RedMagicIndicatorEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(ModEntities.RED_MAGIC_ENTITY.get(), owner, directionX, directionY, directionZ, world);
    }

    public void tick() {
        double radius = 1; // adjust this value to control the size of the circle

        double x = radius * Math.cos(Math.toRadians(degrees));
        double z = radius * Math.sin(Math.toRadians(degrees));

        // set the position of the particle to the calculated x and y values
        this.setPos(x, this.getY(), z); // add a fixed position on top of this

        DustParticleEffect redDust = new DustParticleEffect(new Vec3f(1, 0, 0), 1);
        this.getWorld().addParticle(redDust, this.getX(), this.getY(), this.getZ(), 0, 0, 0);

        this.degrees += 12;
        if (this.degrees == 360) {
            this.degrees = 12;
        }
        super.tick();
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
