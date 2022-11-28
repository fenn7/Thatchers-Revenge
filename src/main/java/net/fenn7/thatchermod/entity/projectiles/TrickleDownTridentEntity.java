package net.fenn7.thatchermod.entity.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.TrickleDownTridentItem;
import net.fenn7.thatchermod.mixin.TridentInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class TrickleDownTridentEntity extends TridentEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private float projectileDmg = 8.0F;

    public TrickleDownTridentEntity(EntityType<? extends TrickleDownTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public TrickleDownTridentEntity(World world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        BlockPos targetPos = entityHitResult.getEntity().getBlockPos();
        Box rangeBox = new Box(targetPos).expand(projectileDmg);

        List<LivingEntity> targets = new ArrayList<>();
        for (Entity entity : world.getOtherEntities(entityHitResult.getEntity(), rangeBox)) {
            if (entity instanceof LivingEntity alive && entity != this.getOwner() &&
                    Math.sqrt(entity.squaredDistanceTo(targetPos.getX(), targetPos.getY(), targetPos.getZ())) <= projectileDmg) {
                targets.add(alive);
            }
        }
        ThatcherMod.LOGGER.warn(targets.toString());

        LivingEntity yes = world.getClosestEntity(targets, TargetPredicate.createAttackable(),
                (LivingEntity) this.getOwner(), targetPos.getX(), targetPos.getY(), targetPos.getZ());

        if (yes != null) {
            ThatcherMod.LOGGER.warn(yes.toString());
            //Vec3d vec3d = new Vec3d(yes.getX() - this.getX(), yes.getBodyY(0.8D) - this.getY(), yes.getZ() - this.getZ()).normalize().multiply(0.67D);
            double d = yes.getX() - this.getX();
            double e = yes.getBodyY(0.4D) - this.getY();
            double f = yes.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            this.setVelocity(d, e + g * 0.2D, f, 1.25F, 0.0F);
            //this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            //this.setVelocity(vec3d);
        }
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.TRICKLE_DOWN_TRIDENT_ENTITY;
    }

    private <E extends IAnimatable> PlayState spinAnimation(AnimationEvent<E> event) {
        if (!this.inGround) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trickle_down_trident.spin", true));
        } else {
            event.getController().setAnimation(null);
        }
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::spinAnimation));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}