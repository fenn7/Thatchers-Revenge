package net.fenn7.thatchermod.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.entity.mobs.RoyalFencerEntity;
import net.fenn7.thatchermod.entity.mobs.RoyalGrenadierEntity;
import net.fenn7.thatchermod.entity.mobs.ThatcherEntity;

public class ModRegistries {

    public static void registerAll() {
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.THATCHER, ThatcherEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.ROYAL_FENCER, RoyalFencerEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.ROYAL_GRENADIER, RoyalGrenadierEntity.setAttributes());
    }
}
