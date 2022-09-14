package net.fenn7.thatchermod.entity.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GrenadeEntity extends AbstractGrenadeEntity implements IAnimatable {
    private static final float EXPLOSION_POWER = 1.35F;
    private static final Block IRON = Blocks.IRON_BLOCK;

    public GrenadeEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public GrenadeEntity(World world, PlayerEntity user) {
        super(ModEntities.GRENADE_ENTITY, world, user);
    }

    public GrenadeEntity(World world, double x, double y, double z) {
        super(ModEntities.GRENADE_ENTITY, world, x, y, z);
    }

    @Override
    protected void initDataTracker() {
        setPower(EXPLOSION_POWER);
        super.initDataTracker();
    }

    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect effect = new BlockStateParticleEffect(ParticleTypes.BLOCK, IRON.getDefaultState());
            for (int i = 0; i < 3; i++) {
                double x = ThreadLocalRandom.current().nextDouble(-this.power, this.power);
                double z = ThreadLocalRandom.current().nextDouble(-this.power, this.power);
                this.world.addParticle(effect, this.getX(), this.getY(), this.getZ(),
                        this.power + x, 2 * this.power, this.power + z);
            }
        }
        else {
            super.handleStatus(status);
        }
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
    }

    @Override
    protected void explode(float power) {
        if (!this.world.isClient()) {
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(this.power, this.power, this.power));
            list.stream().forEach(e -> e.damage(DamageSource.thrownProjectile(this, this.getOwner()), 3.0F));
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), power, Explosion.DestructionType.NONE);
        }
        this.discard();
    }

    protected Item getDefaultItem() {
        return ModItems.GRENADE;
    }
}
