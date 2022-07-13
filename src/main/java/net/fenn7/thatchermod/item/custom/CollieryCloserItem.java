package net.fenn7.thatchermod.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class CollieryCloserItem extends PickaxeItem {
    public static final int DURATION = 36000;

    public CollieryCloserItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            convertCoalItems(world, user, hand);
        }
        return super.use(world, user, hand);
    }

    private void convertCoalItems(World world, PlayerEntity user, Hand hand) {
        ItemStack coalStack = user.getOffHandStack();
        if (coalStack.getItem() == Items.COAL) {
            while (!coalStack.isEmpty()) {
                coalStack.decrement(1);

                int random = ThreadLocalRandom.current().nextInt(0, 99 + 1);
                ItemStack oreStack = null;
                if (0 == random || random == 99) { oreStack = new ItemStack(Items.DIAMOND, 1); }
                else if (1 <= random && random <= 5) { oreStack = new ItemStack(Items.EMERALD, 1); }
                else if (6 <= random && random <= 13) { oreStack = new ItemStack(Items.LAPIS_LAZULI, 1); }
                else if (14 <= random && random <= 23) { oreStack = new ItemStack(Items.GOLD_INGOT, 1); }
                else if (24 <= random && random <= 38) { oreStack = new ItemStack(Items.QUARTZ, 1); }
                else if (39 <= random && random <= 53) { oreStack = new ItemStack(Items.REDSTONE, 1); }
                else if (54 <= random && random <= 73) { oreStack = new ItemStack(Items.COPPER_INGOT, 1); }
                else if (74 <= random && random <= 98) { oreStack = new ItemStack(Items.IRON_INGOT, 1); }

                ItemEntity oreEntity = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), oreStack);
                world.spawnEntity(oreEntity);
            }
        }
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        Direction minerDirection = miner.getMovementDirection();

        Iterable<BlockPos> positions = null;
        switch (minerDirection) {
            case UP, DOWN -> positions = BlockPos.iterate(pos.getX() - 1, pos.getY(), pos.getZ() - 1,
                    pos.getX() + 1, pos.getY(), pos.getZ() + 1);
            case EAST, WEST -> positions = BlockPos.iterate(pos.getX(), pos.getY() - 1, pos.getZ() - 1,
                    pos.getX(), pos.getY() + 1, pos.getZ() + 1);
            case NORTH, SOUTH -> positions = BlockPos.iterate(pos.getX() - 1, pos.getY() - 1, pos.getZ(),
                    pos.getX() + 1, pos.getY() + 1, pos.getZ());
        }

        if (positions != null) {
            for (BlockPos blockPos : positions) {
                if (world.getBlockState(blockPos).getBlock() == world.getBlockState(pos).getBlock()) {
                    world.breakBlock(blockPos, true, miner, 1);
                }
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity) {
            LivingEntity miner = ((LivingEntity) entity);
            Hand hand = miner.getActiveHand();
            if (miner.getStackInHand(hand).getItem() == this) {
                miner.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1));
                miner.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1));
            }
        }
    }

    @Override
    public boolean isSuitableFor(BlockState state) { return true; }
}
