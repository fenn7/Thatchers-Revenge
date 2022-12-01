package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class UnionBusterItem extends ModAxeItem {
    public static final int DURATION = 900;

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
        List<Entity> nearbyEntities = CommonMethods.getEntitiesNearEntity(user, -4, -4, -4, 4, 4, 4, world);
        boolean success = false;

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != user) {
                spawnHitEffects(entity, world);

                double length = entity.getX() - user.getX();
                double width = entity.getZ() - user.getZ();
                double distanceFromUser = (Math.sqrt((length * length) + (width * width))); //pythagoras theorem
                if (distanceFromUser < 1) {
                    distanceFromUser = 1;
                }
                double displacement = (2 / distanceFromUser * 15 + 6); // maximum displacement is 21 blocks

                boolean blockFound = false;
                BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
                for (int i = 0; i < displacement; i++) {
                    pos = pos.offset(Direction.UP, 1);
                    if (!world.getBlockState(pos).isAir()) { // stop on the first NON-AIR block
                        if (pos.getY() % 2 == 1) { // correction needed for odd Y values otherwise they will shoot through block
                            pos = pos.offset(Direction.DOWN, 1);
                        }
                        blockFound = true;
                        break;
                    }
                    i++;
                }

                if (blockFound) {
                    entity.setPosition(entity.getX(), pos.getY() - entity.getHeight(), entity.getZ());
                } else {
                    entity.setPosition(entity.getX(), pos.getY(), entity.getZ());
                }

                ((LivingEntity) entity).setAttacker(user);
                ((LivingEntity) entity).setAttacking(user); // aggros mobs
                user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand)); // -1 durability
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 150,
                        nearbyEntities.size() / 4), user); // every 4 entities hit grants +1 level of resistance
                user.heal(2);
                success = true;
            }
        }
        if (success) {
            CommonMethods.summonDustParticles(world, 1, 1.0f, 0.6f, 0.3f, 3,
                    user.getX(), user.getY() + 2, user.getZ(), 0, 0, 0);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.HOSTILE, 15F, 0.33F);
            user.getItemCooldownManager().set(this, DURATION); //DURATION);
        }
    }

    private void spawnHitEffects(Entity entity, World world) {
        if (!entity.world.isClient) {
            CommonMethods.summonDustParticles(world, 1, 1.0f, 0.6f, 0.3f, 2,
                    entity.getX(), entity.getY() + 0.5D, entity.getZ(), 0, 0, 0);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    }
}
