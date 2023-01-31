package net.fenn7.thatchermod.commonside.util;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.entity.mobs.*;

public class ModRegistries {

    public static void registerAll() {
        registerAttributes();
    }

    private static void registerAttributes() {
        EntityAttributeRegistry.register(ModEntities.THATCHER, ThatcherEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntities.ROYAL_FENCER, RoyalFencerEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntities.ROYAL_GRENADIER, RoyalGrenadierEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntities.FIRST_SPECTRE_ENTITY, FirstSpectreEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntities.SECOND_SPECTRE_ENTITY, SecondSpectreEntity::setAttributes);
        EntityAttributeRegistry.register(ModEntities.THIRD_SPECTRE_ENTITY, ThirdSpectreEntity::setAttributes);
    }
}
