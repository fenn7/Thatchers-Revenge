package net.fenn7.thatchermod.util;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.CollieryCloserItem;
import net.fenn7.thatchermod.item.custom.CommunityChargebowItem;
import net.minecraft.util.Identifier;

public class ModPredicates {
    public static void registerAllPredicates() {
        FabricModelPredicateProviderRegistry.register(ModItems.COLLIERY_CLOSER, new Identifier("isbreaking3x3"),
                (stack, world, entity, seed) -> {
                    return entity != null && CollieryCloserItem.isBreaking3x3(stack) ? 1.0F : 0.0F;
                });

        FabricModelPredicateProviderRegistry.register(ModItems.COMMUNITY_CHARGEBOW, new Identifier("rapid.fire"),
                (stack, world, entity, seed) -> {
                    return entity != null && CommunityChargebowItem.isRapidFiring(stack) ? 1.0F : 0.0F;
                });

        FabricModelPredicateProviderRegistry.register(ModItems.COMMUNITY_CHARGEBOW, new Identifier("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0f;
                    }
                    if (entity.getActiveItem() != stack) {
                        return 0.0f;
                    }
                    if (CommunityChargebowItem.isRapidFiring(stack)) {
                        return (float) 2 * (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0f;
                    } else {
                        return (float) 0.6 * (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0f;
                    }
                });

        FabricModelPredicateProviderRegistry.register(ModItems.COMMUNITY_CHARGEBOW, new Identifier("pulling"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem()
                        && entity.getActiveItem() == stack ? 1.0f : 0.0f);

        FabricModelPredicateProviderRegistry.register(ModItems.TRICKLE_DOWN_TRIDENT, new Identifier("trident.pulling"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem()
                        && entity.getActiveItem() == stack ? 1.0f : 0.0f);
    }
}
