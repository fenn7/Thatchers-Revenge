package net.fenn7.thatchermod.entity.projectiles;

import com.eliotlash.mclib.math.functions.classic.Mod;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SmokeGrenadeEntity extends AbstractGrenadeEntity implements IAnimatable {
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
    protected void explode(float power) {
        BlazeEntity b = new BlazeEntity(EntityType.BLAZE, this.world);
        b.setPosition(this.getX(), this.getY(), this.getZ());
        this.world.spawnEntity(b);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GRENADE_SMOKE;
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
