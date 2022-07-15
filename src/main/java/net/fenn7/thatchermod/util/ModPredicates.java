package net.fenn7.thatchermod.util;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.CollieryCloserItem;
import net.fenn7.thatchermod.item.custom.CommandSceptreItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModPredicates {
    public static void registerAllPredicates() {
        FabricModelPredicateProviderRegistry.register(ModItems.COLLIERY_CLOSER , new Identifier("isbreaking3x3"),
                (stack, world, entity, seed) -> {
                    return entity != null && CollieryCloserItem.isBreaking3x3(stack) ? 1.0F : 0.0F;
                });

        /*FabricModelPredicateProviderRegistry.register(ModItems.COMMAND_SCEPTRE , new Identifier("isdivisible10"),
                (stack, world, entity, seed) -> {
                    return entity != null && CommandSceptreItem.getTicksDivisibleBy(stack, 60) ? 1.0F : 0.0F;
                });*/
    }
}
