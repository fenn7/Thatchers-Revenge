package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class MilkSnatcherItem extends SwordItem {
    public static final int DURATION = 900;

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
        if (target.hasStatusEffect(ModEffects.CALCIUM_DEFICIENCY)) {
            target.damage(DamageSource.MAGIC, this.getAttackDamage()/3);
        }
        return super.postHit(stack, target, attacker);
    }

    private void drainCalciumFromEntities(World world, PlayerEntity user, Hand hand) {
        List<Entity> nearbyEntities = CommonMethods.getEntitiesNearEntity(user, -4, -4, -4, 4, 4, 4, world);
        boolean success = false;

        for (Entity entity: nearbyEntities) {
            if (entity instanceof LivingEntity && entity != user) {
                spawnHitEffects(entity, world);
                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(ModEffects.CALCIUM_DEFICIENCY, 300), user);
                ((LivingEntity) entity).setAttacker(user); ((LivingEntity) entity).setAttacking(user); // aggros mobs
                user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand)); // -1 durability for each
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 150,
                        (int) (nearbyEntities.size()/4)), user); // every 4 entities hit grants +1 level of strength
                success = true;
            }
        }

        if (success) {
            CommonMethods.summonDustParticles(world, 1, 1.0f, 1.0f, 1.0f, 3,
                    user.getX(), user.getY() + 2, user.getZ(), 0, 0, 0);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_STRAY_DEATH, SoundCategory.HOSTILE, 15F, 0.66F);
            user.getItemCooldownManager().set(this, DURATION);
        }
    }

    private void spawnHitEffects(Entity entity, World world) {
        CommonMethods.summonDustParticles(world, 1, 1.0f, 1.0f, 1.0f, 2,
                entity.getX(), entity.getY() + 0.5D, entity.getZ(), 0, 0, 0);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    }
}
