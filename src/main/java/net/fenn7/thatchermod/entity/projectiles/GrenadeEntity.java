package net.fenn7.thatchermod.entity.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GrenadeEntity extends AbstractGrenadeEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final int MAX_AGE = 100;
    private static final float EXPLOSION_POWER = 1.0F;
    private static final Block IRON = Blocks.IRON_BLOCK;

    public GrenadeEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        initAgeAndPower(MAX_AGE, EXPLOSION_POWER);
    }

    public GrenadeEntity(World world, PlayerEntity user) {
        super(ModEntities.GRENADE_ENTITY, world, user);
        initAgeAndPower(MAX_AGE, EXPLOSION_POWER);
    }

    public GrenadeEntity(World world, double x, double y, double z) {
        super(ModEntities.GRENADE_ENTITY, world, x, y, z);
        initAgeAndPower(MAX_AGE, EXPLOSION_POWER);
    }

    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect effect = new BlockStateParticleEffect(ParticleTypes.BLOCK, IRON.getDefaultState());
            for (int i = 0; i < 3; i++) {
                double x = ThreadLocalRandom.current().nextDouble(-EXPLOSION_POWER, EXPLOSION_POWER);
                double z = ThreadLocalRandom.current().nextDouble(-EXPLOSION_POWER, EXPLOSION_POWER);
                this.world.addParticle(effect, this.getX(), this.getY(), this.getZ(),
                        EXPLOSION_POWER + x, 2 * EXPLOSION_POWER, EXPLOSION_POWER + z);
            }
        }
    }


    public void tick() {
        if (this.age == 1) {
            world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.HOSTILE,
                    1.0F, 1.0F, true);
        }
        super.tick();
    }

    protected void onCollision(HitResult hitResult) {
        if (!this.world.isClient()) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                // testing direction. may be used for bounce physics in future.
                ThatcherMod.LOGGER.warn(((BlockHitResult) hitResult).getSide().asString());
            }
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(1.5D, 1.5D, 1.5D));
            list.stream().forEach(e -> e.damage(DamageSource.thrownProjectile(this, this.getOwner()), 4.0F));
            explode();
        }
        super.onCollision(hitResult);
    }

    protected Item getDefaultItem() {
        return ModItems.GRENADE;
    }

    private <E extends IAnimatable> PlayState flyingAnimation(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.grenade.flying", true));
        return PlayState.CONTINUE;
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
