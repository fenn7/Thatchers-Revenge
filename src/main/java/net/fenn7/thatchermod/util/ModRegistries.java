package net.fenn7.thatchermod.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.entity.custom.ParamilitaryEntity;
import net.fenn7.thatchermod.entity.custom.ThatcherEntity;

public class ModRegistries {

    public static void registerAll() {
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.THATCHER, ThatcherEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.PARAMILITARY, ParamilitaryEntity.setAttributes());
    }
}
