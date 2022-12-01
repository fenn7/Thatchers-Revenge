package net.fenn7.thatchermod.util;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.entity.mobs.RoyalFencerEntity;
import net.fenn7.thatchermod.entity.mobs.RoyalGrenadierEntity;
import net.fenn7.thatchermod.entity.mobs.ThatcherEntity;

public class ModRegistries {

    public static void registerAll() {
        registerAttributes();
    }

    private static void registerAttributes() {
        EntityAttributeRegistry.register(ModEntities.THATCHER, ThatcherEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntities.ROYAL_FENCER, RoyalFencerEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntities.ROYAL_GRENADIER, RoyalGrenadierEntity::setAttributes);
    }
}
