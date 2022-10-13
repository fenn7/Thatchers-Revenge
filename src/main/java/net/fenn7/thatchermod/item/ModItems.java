package net.fenn7.thatchermod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.custom.*;
import net.fenn7.thatchermod.item.custom.grenade.FireGrenadeItem;
import net.fenn7.thatchermod.item.custom.grenade.GrenadeItem;
import net.fenn7.thatchermod.item.custom.grenade.GrenadeLauncherItem;
import net.fenn7.thatchermod.item.custom.grenade.SmokeGrenadeItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
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
    public static final Item HARDENED_FLESH_OF_THATCHER = registerItem("hardened_flesh_of_thatcher",
            new Item(new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().rarity(Rarity.EPIC)));
    public static final Item ESSENCE_OF_THATCHER = registerItem("essence_of_thatcher",
            new Item(new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().rarity(Rarity.EPIC)));

    public static final Item UNION_BUSTER = registerItem("union_buster",
            new UnionBusterItem(ModToolMaterials.THATCHERITE, 6.5F, -2.75F, new FabricItemSettings().group(ModItemGroup.THATCHER)
                    .fireproof().maxCount(1).rarity(Rarity.RARE)));
    public static final Item MILK_SNATCHER = registerItem("milk_snatcher",
            new MilkSnatcherItem(ModToolMaterials.THATCHERITE, 3, -2.0F, new FabricItemSettings().group(ModItemGroup.THATCHER)
                    .fireproof().maxCount(1).rarity(Rarity.RARE)));
    public static final Item COLLIERY_CLOSER = registerItem("colliery_closer",
            new CollieryCloserItem(ModToolMaterials.THATCHERITE, 1, -2.4F, new FabricItemSettings().group(ModItemGroup.THATCHER)
                    .fireproof().maxCount(1).rarity(Rarity.RARE)));
    public static final Item COMMUNITY_CHARGEBOW = registerItem("community_chargebow",
            new CommunityChargebowItem(new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof()
                    .maxCount(1).maxDamage(805).maxDamageIfAbsent(805).rarity(Rarity.RARE)));
    public static final Item COMMAND_SCEPTRE = registerItem("command_sceptre_3d",
            new CommandSceptreItem(new FabricItemSettings().group(ModItemGroup.THATCHER)
                    .fireproof().maxCount(1).maxDamage(405).maxDamageIfAbsent(405).rarity(Rarity.RARE)));
    public static final Item THATCHERITE_HELMET = registerItem("thatcherite_helmet",
            new ThatcheriteArmourItem(ModArmourMaterials.THATCHERITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().maxCount(1).rarity(Rarity.RARE)));
    public static final Item THATCHERITE_CHESTPLATE = registerItem("thatcherite_chestplate",
            new ThatcheriteArmourItem(ModArmourMaterials.THATCHERITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().maxCount(1).rarity(Rarity.RARE)));
    public static final Item THATCHERITE_GREAVES = registerItem("thatcherite_greaves",
            new ThatcheriteArmourItem(ModArmourMaterials.THATCHERITE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().maxCount(1).rarity(Rarity.RARE)));
    public static final Item THATCHERITE_BOOTS = registerItem("thatcherite_boots",
            new ThatcheriteArmourItem(ModArmourMaterials.THATCHERITE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.THATCHER).fireproof().maxCount(1).rarity(Rarity.RARE)));

    public static final Item GRENADE = registerItem("grenade",
            new GrenadeItem(new FabricItemSettings().group(ModItemGroup.THATCHER).maxCount(16)));
    public static final Item GRENADE_FIRE = registerItem("grenade_fire",
            new FireGrenadeItem(new FabricItemSettings().group(ModItemGroup.THATCHER).maxCount(12)));
    public static final Item GRENADE_SMOKE = registerItem("grenade_smoke",
            new SmokeGrenadeItem(new FabricItemSettings().group(ModItemGroup.THATCHER).maxCount(8)));
    public static final Item GRENADE_LAUNCHER = registerItem("grenade_launcher",
            new GrenadeLauncherItem(new FabricItemSettings().group(ModItemGroup.THATCHER).maxCount(1)
                    .maxDamage(512).maxDamageIfAbsent(512)));

    public static final Item PARAMILITARY_SPAWN_EGG = registerItem("fencer_spawn_egg",
            new SpawnEggItem(ModEntities.ROYAL_FENCER, 0x000000, 0x78582F,
                    new FabricItemSettings().group(ModItemGroup.THATCHER)));

    public static void registerModItems(){
        ThatcherMod.LOGGER.debug("Registering Items for " + ThatcherMod.MOD_ID + " ...");
    }

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(ThatcherMod.MOD_ID, name), item);
    }
}
