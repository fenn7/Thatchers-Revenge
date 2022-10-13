package net.fenn7.thatchermod.entity.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RoyalGrenadierEntity extends AbstractMilitaryEntity {
    protected RoyalGrenadierEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    /*private static class ShootGrenadeGoal extends Goal {
        private final RoyalGrenadierEntity ghast;
        public int cooldown;

        public ShootFireballGoal(RoyalGrenadierEntity ghast) {
            this.ghast = ghast;
        }

        public boolean canStart() {
            return this.ghast.getTarget() != null;
        }

        public void start() {
            this.cooldown = 0;
        }

        public void stop() {
            this.ghast.setShooting(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.ghast.getTarget();
            if (livingEntity != null) {
                double d = 64.0D;
                if (livingEntity.squaredDistanceTo(this.ghast) < 4096.0D && this.ghast.canSee(livingEntity)) {
                    World world = this.ghast.world;
                    ++this.cooldown;
                    if (this.cooldown == 10 && !this.ghast.isSilent()) {
                        world.syncWorldEvent((PlayerEntity)null, 1015, this.ghast.getBlockPos(), 0);
                    }

                    if (this.cooldown == 20) {
                        double e = 4.0D;
                        Vec3d vec3d = this.ghast.getRotationVec(1.0F);
                        double f = livingEntity.getX() - (this.ghast.getX() + vec3d.x * 4.0D);
                        double g = livingEntity.getBodyY(0.5D) - (0.5D + this.ghast.getBodyY(0.5D));
                        double h = livingEntity.getZ() - (this.ghast.getZ() + vec3d.z * 4.0D);
                        if (!this.ghast.isSilent()) {
                            world.syncWorldEvent((PlayerEntity)null, 1016, this.ghast.getBlockPos(), 0);
                        }

                        FireballEntity fireballEntity = new FireballEntity(world, this.ghast, f, g, h, this.ghast.getFireballStrength());
                        fireballEntity.setPosition(this.ghast.getX() + vec3d.x * 4.0D, this.ghast.getBodyY(0.5D) + 0.5D, fireballEntity.getZ() + vec3d.z * 4.0D);
                        world.spawnEntity(fireballEntity);
                        this.cooldown = -40;
                    }
                } else if (this.cooldown > 0) {
                    --this.cooldown;
                }

                this.ghast.setShooting(this.cooldown > 10);
            }
        }
    }*/
}
