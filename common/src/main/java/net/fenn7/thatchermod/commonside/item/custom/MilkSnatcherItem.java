package net.fenn7.thatchermod.commonside.item.custom;

import net.fenn7.thatchermod.commonside.effect.ModEffects;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.util.CommonMethods;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MilkSnatcherItem extends SwordItem {
    public static final int DURATION = 900;
    private static final double RANGE = 5.0D;

    public MilkSnatcherItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            drainCalciumFromEntities(world, user, hand);
        }
        return super.use(world, user, hand);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.hasStatusEffect(ModEffects.CALCIUM_DEFICIENCY.get())) {
            target.damage(DamageSource.MAGIC, this.getAttackDamage() / 4);
        }
        return super.postHit(stack, target, attacker);
    }

    private void drainCalciumFromEntities(World world, PlayerEntity user, Hand hand) {
        List<Entity> nearbyEntities = CommonMethods.getEntitiesNearEntity(user,
                -RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE, world);
        boolean success = false;
        for (Entity entity : nearbyEntities) {
            double distanceFrom = entity.distanceTo(user);
            if (entity instanceof LivingEntity living && entity != user && distanceFrom <= RANGE) {
                spawnHitEffects(entity, world);
                if (living instanceof AbstractSkeletonEntity skeleton) {
                    world.spawnEntity(new ItemEntity(world, skeleton.getX(), skeleton.getBodyY(0.9), skeleton.getZ(), getSkullStack(skeleton)));
                    for (int random = 2; random > 0; random--) {
                        world.spawnEntity(new ItemEntity(world, skeleton.getX(), skeleton.getBodyY(random * 0.2D + 0.3D), skeleton.getZ(), new ItemStack(Items.BONE_MEAL),
                                ThreadLocalRandom.current().nextDouble(-0.1F, 0.1F), 0, ThreadLocalRandom.current().nextDouble(-0.1F, 0.1F)));
                    }
                    skeleton.remove(Entity.RemovalReason.KILLED);
                } else {
                    living.addStatusEffect(new StatusEffectInstance(ModEffects.CALCIUM_DEFICIENCY.get(), 300), user);
                    living.setAttacker(user); living.setAttacking(user); // aggros mobs
                }
                user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 150,
                        nearbyEntities.size() / 4), user); // every 4 entities hit grants +1 level of strength
                if (!success) success = true;
            }
        }
        if (success) {
            CommonMethods.summonDustParticles(world, 1, 1.0f, 1.0f, 1.0f, 3,
                    user.getX(), user.getY() + 2, user.getZ(), 0, 0, 0);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_STRAY_DEATH, SoundCategory.HOSTILE, 15F, 0.66F);
            user.getItemCooldownManager().set(this, DURATION);
        }
    }

    private ItemStack getSkullStack(AbstractSkeletonEntity skeleton) {
        return skeleton instanceof WitherSkeletonEntity ? new ItemStack(Items.WITHER_SKELETON_SKULL) : new ItemStack(Items.SKELETON_SKULL);
    }
    private void spawnHitEffects(Entity entity, World world) {
        CommonMethods.summonDustParticles(world, 1, 1.0f, 1.0f, 1.0f, 2,
                entity.getX(), entity.getY() + 0.5D, entity.getZ(), 0, 0, 0);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    }
}
