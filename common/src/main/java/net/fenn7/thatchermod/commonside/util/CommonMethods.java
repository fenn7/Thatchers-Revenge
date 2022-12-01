package net.fenn7.thatchermod.commonside.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.List;

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

    static BlockPos findFirstNonAirBlockDown(World world, BlockPos pos) {
        BlockPos returnPos = pos;
        boolean found = false;
        while (world.getBlockState(returnPos).isAir() && returnPos.getY() >= -64 && !found) {
            returnPos = returnPos.offset(Direction.DOWN, 1);
            if (!world.getBlockState(returnPos).isAir()) {
                found = true;
                break;
            }
        }
        return returnPos;
    }

    static void findFirstNonAirBlockUp() {

    }
}
