package net.fenn7.thatchermod.block.entity.custom;

import net.fenn7.thatchermod.block.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CursedMeteorEntity extends ExplosiveProjectileEntity {

    public CursedMeteorEntity(EntityType<? extends CursedMeteorEntity> entityType, World world) {
        super(entityType, world);
    }

    public CursedMeteorEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(ModEntities.CURSED_METEOR, owner, directionX, directionY, directionZ, world);
    }
}
