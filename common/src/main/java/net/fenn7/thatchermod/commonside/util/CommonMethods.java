package net.fenn7.thatchermod.commonside.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface CommonMethods {

    static List<Entity> getEntitiesNearEntity(Entity user, double x1, double y1, double z1, double x2,
                                              double y2, double z2, World world) {
        BlockPos pos1 = new BlockPos(user.getX() + x1, user.getY() + y1, user.getZ() + z1);
        BlockPos pos2 = new BlockPos(user.getX() + x2, user.getY() + y2, user.getZ() + z2);

        Box box = new Box(pos1, pos2);
        ItemEntity item = new ItemEntity(EntityType.ITEM, world);
        return world.getOtherEntities(item, box);
    }

    static void summonDustParticles(World world, int number, float red, float green, float blue, float size,
                                    double x, double y, double z, double velX, double velY, double velZ) {
        DustParticleEffect dust = new DustParticleEffect(new Vec3f(red, green, blue), size);
        ((ServerWorld) world).spawnParticles(dust, x, y, z, number, velX, velY, velZ, 0);
    }

    static void spawnParticleCircle(World world, ParticleEffect particle, double x, double y, double z, double radius) {
        for (double angle = 0; angle < 360; angle += 24) {
            double radians = Math.toRadians(angle);
            double particleX = Math.round(x + radius * Math.cos(radians));
            double particleY = Math.round(y + radius * Math.sin(radians));
            if (world.isClient) {
                world.addParticle(particle, particleX, particleY, z, 0, 0, 0);
            } else {
                ((ServerWorld) world).spawnParticles(particle, particleX, particleY, z, 1, 0, 0, 0, 0);
            }
        }
    }

    static BlockPos findFirstNonAirBlockDown(World world, BlockPos pos) {
        BlockPos returnPos = pos;
        boolean found = false;
        while (world.getBlockState(returnPos).isAir() && returnPos.getY() >= world.getBottomY() && !found) {
            returnPos = returnPos.offset(Direction.DOWN, 1);
            if (!world.getBlockState(returnPos).isAir()) {
                found = true;
                break;
            }
        }
        return returnPos;
    }

    static List<Entity> getAllEntityCollisions(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double bonusRange) {
        double d = Double.MAX_VALUE;
        List<Entity> entities = new ArrayList<>();

        for(Entity entity3 : world.getOtherEntities(entity, box, predicate)) {
            Box box2 = entity3.getBoundingBox().expand(bonusRange);
            Optional<Vec3d> optional = box2.raycast(min, max);
            if (optional.isPresent()) {
                entities.add(entity3);
            }
        }

        return entities;
    }
}
