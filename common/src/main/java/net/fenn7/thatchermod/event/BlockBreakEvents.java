package net.fenn7.thatchermod.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.utils.value.IntValue;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public enum BlockBreakEvents implements BlockEvent.Break {

    INSTANCE;

    @Override
    public EventResult breakBlock(World world, BlockPos pos, BlockState state, ServerPlayerEntity player, @Nullable IntValue xp) {
        int level = EnchantmentHelper.getLevel(ModEnchantments.PRIVATISATION.get(), player.getMainHandStack());
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
        return EventResult.pass();
    }
}

