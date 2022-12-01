package net.fenn7.thatchermod.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.BLOCK_KEY);

    public static final RegistrySupplier<Block> THATCHERISM_ALTAR = BLOCKS.register(
            "thatcherism_altar",
            () -> new ThatcherismAltarBlock(AbstractBlock.Settings.of(Material.STONE)
                    .strength(42.0F, 1337.0F).requiresTool()
                    .luminance(
                            (state) -> (state.get(ThatcherismAltarBlock.IS_CHANNELING) || state.get(ThatcherismAltarBlock.IS_PREPARED)) ? 15 : 0
                    ))
    );

    public static void registerModBlocks() {
        ThatcherMod.LOGGER.debug("Registering Blocks for " + ThatcherMod.MOD_ID + " ...");
        BLOCKS.register();
    }
}
