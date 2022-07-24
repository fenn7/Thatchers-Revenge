package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BlockBreakEvents implements PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        int level = EnchantmentHelper.getLevel(ModEnchantments.PRIVATISATION, player.getMainHandStack());
        if (level != 0) { // override postmine instead ?
            BlockPos pos1 = new BlockPos(pos.getX() - level, pos.getY() - level, pos.getZ() - level);
            BlockPos pos2 = new BlockPos(pos.getX() + level, pos.getY() + level, pos.getZ() + level);
            Box box = new Box(pos1, pos2);

            List<Entity> entityList = world.getOtherEntities(null, box);
            for (Entity entity : entityList) {
                if (entity instanceof ItemEntity item) {
                    player.giveItemStack(item.getStack());
                }
            }

            if (state.isOf(Blocks.COAL_ORE) || state.isOf(Blocks.DEEPSLATE_COAL_ORE)) {
                for (int j = 0; j < level; j++) {
                    int random = ThreadLocalRandom.current().nextInt(0, 99 + 1);
                    if (random == 69) {
                        ItemEntity goldEntity = new ItemEntity(player.world, player.getX(), player.getY(), player.getZ(),
                                new ItemStack(Items.GOLD_INGOT, 1));
                        world.spawnEntity(goldEntity);
                    }
                }
            }
        }
    }
}

