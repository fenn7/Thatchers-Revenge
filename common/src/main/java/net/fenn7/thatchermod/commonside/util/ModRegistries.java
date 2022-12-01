package net.fenn7.thatchermod.commonside.util;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.entity.mobs.RoyalFencerEntity;
import net.fenn7.thatchermod.commonside.entity.mobs.RoyalGrenadierEntity;
import net.fenn7.thatchermod.commonside.entity.mobs.ThatcherEntity;

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
