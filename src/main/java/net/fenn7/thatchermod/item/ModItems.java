package net.fenn7.thatchermod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.item.custom.ThatcherSoulItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item HEART_OF_THATCHER = registerItem("heart_of_thatcher",
            new Item(new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().maxCount(1).rarity(Rarity.RARE)
                    .food(ModFoodComponents.HEART_OF_THATCHER)));
    public static final Item SOUL_OF_THATCHER = registerItem("soul_of_thatcher",
            new ThatcherSoulItem(new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().maxCount(1).rarity(Rarity.RARE)
                    .food(ModFoodComponents.SOUL_OF_THATCHER)));

    public static void registerModItems(){
        ThatcherMod.LOGGER.debug("Registering Items for " + ThatcherMod.MOD_ID + " ...");
    }

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(ThatcherMod.MOD_ID, name), item);
    }
}
