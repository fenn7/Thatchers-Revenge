package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public int age;

    @Shadow
    public World world;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract float getPitch();

    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract void setPitch(float pitch);

    @Inject(method = "onKilledOther", at = @At("HEAD"))
    protected void thatchersRevenge$setLastStandEffect(ServerWorld world, LivingEntity other, CallbackInfo ci) {
    }
}
