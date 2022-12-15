package net.fenn7.thatchermod.client.util;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.CollieryCloserItem;
import net.fenn7.thatchermod.commonside.item.custom.CommunityChargebowItem;
import net.minecraft.util.Identifier;

public class ModPredicates {

    public static void registerAllPredicates() {
        ModItems.COLLIERY_CLOSER.listen(item -> ItemPropertiesRegistry.register(
                item,
                new Identifier("isbreaking3x3"),
                (stack, world, entity, seed) -> entity != null && CollieryCloserItem.isBreaking3x3(stack) ? 1.0F : 0.0F
        ));

        ModItems.COMMUNITY_CHARGEBOW.listen(item -> {
            ItemPropertiesRegistry.register(
                    item,
                    new Identifier("rapid.fire"),
                    (stack, world, entity, seed) -> entity != null && CommunityChargebowItem.isRapidFiring(stack) ? 1.0F : 0.0F
            );
            ItemPropertiesRegistry.register(
                    item,
                    new Identifier("pull"),
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
                    }
            );
            ItemPropertiesRegistry.register(
                    item,
                    new Identifier("pulling"),
                    (stack, world, entity, seed) -> entity != null
                            && entity.isUsingItem()
                            && entity.getActiveItem() == stack ? 1.0f : 0.0f
            );
        });

        ModItems.TRICKLE_DOWN_TRIDENT.listen(item -> ItemPropertiesRegistry.register(
                item,
                new Identifier("trident.pulling"),
                (stack, world, entity, seed) -> entity != null
                        && entity.isUsingItem()
                        && entity.getActiveItem() == stack ? 1.0f : 0.0f
        ));
    }
}
