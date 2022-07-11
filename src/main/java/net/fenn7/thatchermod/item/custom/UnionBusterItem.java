package net.fenn7.thatchermod.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class UnionBusterItem extends ModAxeItem {
    public static final int DURATION = 1200;

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
        BlockPos pos1 = new BlockPos(user.getX() - 4, user.getY(), user.getZ() - 4);
        BlockPos pos2 = new BlockPos(user.getX() + 4, user.getY() + 4, user.getZ() + 4);

        Box box = new Box(pos1, pos2);
        List<Entity> nearbyEntities = world.getOtherEntities(null, box);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != user) {
                double length = entity.getX() - user.getX();
                double width = entity.getZ() - user.getZ();
                double distanceFromUser = (Math.sqrt((length * length) + (width * width))); //pythagoras theorem
                if (distanceFromUser < 1) { distanceFromUser = 1; }
                double displacement = (1 / distanceFromUser * 15 + 4); // maximum displacement is 19 blocks

                boolean blockFound = false;
                BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
                for (int i = 0; i < displacement; i++) {
                    pos = new BlockPos(entity.getX(), entity.getY() + i, entity.getZ());
                    if (!world.getBlockState(pos).isAir()) { // stop on the first NON-AIR block
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

                ((LivingEntity) entity).setAttacker(user); ((LivingEntity) entity).setAttacking(user); // aggros mobs
                user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand)); // -1 durability
                user.heal(2);
            }
        }
        user.getItemCooldownManager().set(this, DURATION);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }
}
