package net.fenn7.thatchermod.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fenn7.thatchermod.block.entity.ModEntities;
import net.fenn7.thatchermod.block.entity.custom.ThatcherEntity;

public class ModRegistries {

    public static void registerAll() {
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.THATCHER, ThatcherEntity.setAttributes());
    }
}
