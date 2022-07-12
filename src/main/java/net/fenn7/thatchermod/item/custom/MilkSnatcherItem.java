package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.ModStatus;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class MilkSnatcherItem extends SwordItem implements CommonMethods {
    public static final int DURATION = 1200;

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
            target.damage(DamageSource.MAGIC, 999999.0F);
            target.pushAwayFrom(attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    private void drainCalciumFromEntities(World world, PlayerEntity user, Hand hand) {
        List<Entity> nearbyEntities = getEntitiesNearPlayer(user, -4, 0, -4, 4, 4, 4, world);

        for (Entity entity: nearbyEntities) {
            if (entity instanceof LivingEntity && entity != user) {
                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(ModEffects.CALCIUM_DEFICIENCY, 200), user);
                ((LivingEntity) entity).setAttacker(user); ((LivingEntity) entity).setAttacking(user); // aggros mobs
                user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand)); // -1 durability
            }
        }
    }
}
