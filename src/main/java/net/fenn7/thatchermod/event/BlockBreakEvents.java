package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class BlockBreakEvents implements PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        int level = EnchantmentHelper.getLevel(ModEnchantments.PRIVATISATION, player.getMainHandStack());
        if (level != 0) {
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

