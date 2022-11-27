package net.fenn7.thatchermod.entity.projectiles;

import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.TrickleDownTridentItem;
import net.fenn7.thatchermod.mixin.TridentInterface;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class TrickleDownTridentEntity extends TridentEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public TrickleDownTridentEntity(EntityType<? extends TrickleDownTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public TrickleDownTridentEntity(World world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.TRICKLE_DOWN_TRIDENT_ENTITY;
    }

    private <E extends IAnimatable> PlayState spinAnimation(AnimationEvent<E> event) {
        if (this.getVelocity() != Vec3d.ZERO) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trickle_down_trident.spin", true));
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