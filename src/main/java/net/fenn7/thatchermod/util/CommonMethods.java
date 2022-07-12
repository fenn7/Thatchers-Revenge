package net.fenn7.thatchermod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public interface CommonMethods {

    public default List<Entity> getEntitiesNearPlayer(PlayerEntity user, double x1, double y1, double z1, double x2,
                                                 double y2, double z2, World world) {
        BlockPos pos1 = new BlockPos(user.getX() + x1, user.getY() + y1, user.getZ() + z1);
        BlockPos pos2 = new BlockPos(user.getX() + x2, user.getY() + y2, user.getZ() + z2);

        Box box = new Box(pos1, pos2);
        return world.getOtherEntities(null, box);
    }
}
