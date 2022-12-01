package net.fenn7.thatchermod.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.block.blockentity.ThatcherismAltarBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY);

    public static final RegistrySupplier<BlockEntityType<ThatcherismAltarBlockEntity>> THATCHERISM_ALTAR = BLOCK_ENTITY_TYPES.register(
            "thatcherism_altar",
            () -> BlockEntityType.Builder.create(
                    ThatcherismAltarBlockEntity::new,
                    ModBlocks.THATCHERISM_ALTAR.get()
            ).build(null)
    );

    public static void registerModBlockEntities() {
        ThatcherMod.LOGGER.debug("Registering Block Entities for " + ThatcherMod.MOD_ID + " ...");
        BLOCK_ENTITY_TYPES.register();
    }
}
