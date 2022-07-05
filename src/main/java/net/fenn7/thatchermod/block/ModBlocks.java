package net.fenn7.thatchermod.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock;
import net.fenn7.thatchermod.item.ModItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block THATCHERISM_ALTAR = registerBlock("thatcherism_altar",
            new ThatcherismAltarBlock(FabricBlockSettings.of(Material.STONE)
                    .strength(42.0F,1337.0F).requiresTool()
                    .luminance((state) ->
                            (state.get(ThatcherismAltarBlock.IS_CHANNELING) || state.get(ThatcherismAltarBlock.IS_PREPARED))
                                    ? 15 : 0)), ModItemGroup.THATCHER);

    private static Block registerBlock(String name, Block block, ItemGroup group){
        registerBlockAsItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(ThatcherMod.MOD_ID, name), block);
    }

    private static Item registerBlockAsItem(String name, Block block, ItemGroup group){
        return Registry.register(Registry.ITEM, new Identifier(ThatcherMod.MOD_ID, name), new BlockItem(block,
                new FabricItemSettings().group(group).fireproof().rarity(Rarity.EPIC)));
    }

    public static void registerModBlocks() {
        ThatcherMod.LOGGER.debug("Registering Blocks for " + ThatcherMod.MOD_ID + " ...");
    }
}
