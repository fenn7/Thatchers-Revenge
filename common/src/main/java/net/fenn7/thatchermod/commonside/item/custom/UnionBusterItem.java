package net.fenn7.thatchermod.commonside.item.custom;

import net.fenn7.thatchermod.commonside.util.CommonMethods;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class UnionBusterItem extends AxeItem {
    public static final int DURATION = 900;
    private static final double RANGE = 5.0D;

    public UnionBusterItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            launchEntitiesUpwards(world, user, hand);
        }
        return super.use(world, user, hand);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 1));
        return super.postHit(stack, target, attacker);
    }

    private void launchEntitiesUpwards(World world, PlayerEntity user, Hand hand) {
        List<Entity> nearbyEntities = CommonMethods.getEntitiesNearEntity(user,
                -RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE, world);
        boolean success = false;
        for (Entity entity : nearbyEntities) {
            double distanceFrom = entity.distanceTo(user);
            if (entity instanceof LivingEntity living && entity != user && distanceFrom <= RANGE) {
                spawnHitEffects(entity, world);
                if (distanceFrom < 1)  distanceFrom = 1;
                double displacement = (2 / distanceFrom * 15 + 6); // maximum displacement is 36 blocks
                living.move(MovementType.SELF, new Vec3d(0, displacement, 0));

                living.setAttacker(user); living.setAttacking(user); // aggros mobs
                user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 150,
                        nearbyEntities.size() / 4), user); // every 4 entities hit grants +1 level of resistance
                user.heal(1.5F);
                if (!success) success = true;
            }
        }
        if (success) {
            CommonMethods.spawnParticleCircle(world, new BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(user.getBlockPos())),
                                                user.getX(), user.getY() + 0.5D, user.getZ(), RANGE / 2);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.HOSTILE, 15F, 0.33F);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.HOSTILE, 10F, 0.1F);
            user.getItemCooldownManager().set(this, DURATION);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        CommonMethods.spawnParticleCircle(world, new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState()),
                user.getX(), user.getY() + 0.5D, user.getZ(), RANGE);
        return super.finishUsing(stack, world, user);
    }

    private void spawnHitEffects(Entity entity, World world) {
        if (!entity.world.isClient) {
            CommonMethods.summonDustParticles(world, 1, 1.0f, 0.6f, 0.3f, 2,
                    entity.getX(), entity.getY() + 0.5D, entity.getZ(), 0, 0, 0);
        }
    }
}
