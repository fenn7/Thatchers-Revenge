package net.fenn7.thatchermod.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.block.blockentity.ThatcherismAltarBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {

    public static BlockEntityType<ThatcherismAltarBlockEntity> THATCHERISM_ALTAR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "thatcherism_altar"),
            FabricBlockEntityTypeBuilder.create(ThatcherismAltarBlockEntity::new,
                    ModBlocks.THATCHERISM_ALTAR).build(null));

    public static void registerModBlockEntities() {
        ThatcherMod.LOGGER.debug("Registering Block Entities for " + ThatcherMod.MOD_ID + " ...");
    }
}
