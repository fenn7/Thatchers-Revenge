package net.fenn7.thatchermod.commonside.item;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.block.ModBlocks;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.item.custom.*;
import net.fenn7.thatchermod.commonside.item.custom.grenade.FireGrenadeItem;
import net.fenn7.thatchermod.commonside.item.custom.grenade.GrenadeItem;
import net.fenn7.thatchermod.commonside.item.custom.grenade.GrenadeLauncherItem;
import net.fenn7.thatchermod.commonside.item.custom.grenade.SmokeGrenadeItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.ITEM_KEY);

    public static final RegistrySupplier<Item> HEART_OF_THATCHER = ITEMS.register(
            "heart_of_thatcher",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .fireproof()
                    .maxCount(1)
                    .rarity(Rarity.RARE)
                    .food(ModFoodComponents.HEART_OF_THATCHER)
            )
    );

    public static final RegistrySupplier<Item> SOUL_OF_THATCHER = ITEMS.register(
            "soul_of_thatcher",
            () -> new ThatcherSoulItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .fireproof()
                    .maxCount(1)
                    .rarity(Rarity.RARE)
                    .food(ModFoodComponents.SOUL_OF_THATCHER)
            )
    );

    public static final RegistrySupplier<Item> HARDENED_FLESH_OF_THATCHER = ITEMS.register(
            "hardened_flesh_of_thatcher",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .fireproof()
                    .rarity(Rarity.EPIC)
            )
    );

    public static final RegistrySupplier<Item> ESSENCE_OF_THATCHER = ITEMS.register(
            "essence_of_thatcher",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .fireproof()
                    .rarity(Rarity.EPIC)
            )
    );

    public static final RegistrySupplier<Item> UNION_BUSTER = ITEMS.register(
            "union_buster",
            () -> new UnionBusterItem(
                    ModToolMaterials.THATCHERITE,
                    6.5F,
                    -2.75F,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
                            .fireproof()
                            .maxCount(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> MILK_SNATCHER = ITEMS.register(
            "milk_snatcher",
            () -> new MilkSnatcherItem(
                    ModToolMaterials.THATCHERITE,
                    3,
                    -2.0F,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
                            .fireproof()
                            .maxCount(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> COLLIERY_CLOSER = ITEMS.register(
            "colliery_closer",
            () -> new CollieryCloserItem(
                    ModToolMaterials.THATCHERITE,
                    1,
                    -2.4F,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
                            .fireproof()
                            .maxCount(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> COMMUNITY_CHARGEBOW = ITEMS.register(
            "community_chargebow",
            () -> new CommunityChargebowItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .fireproof()
                    .maxCount(1)
                    .maxDamage(805)
                    .maxDamageIfAbsent(805)
                    .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> COMMAND_SCEPTRE = ITEMS.register(
            "command_sceptre_3d",
            () -> new CommandSceptreItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .fireproof()
                    .maxCount(1)
                    .maxDamage(405)
                    .maxDamageIfAbsent(405)
                    .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> TRICKLE_DOWN_TRIDENT = ITEMS.register(
            "trickle_down_trident",
            () -> new TrickleDownTridentItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .fireproof()
                    .maxCount(1)
                    .maxDamage(611)
                    .maxDamageIfAbsent(611)
                    .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> THATCHERITE_HELMET = ITEMS.register(
            "thatcherite_helmet",
            () -> ThatcheriteArmourItem.create(
                    ModArmourMaterials.THATCHERITE,
                    EquipmentSlot.HEAD,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
                            .fireproof()
                            .maxCount(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> THATCHERITE_CHESTPLATE = ITEMS.register(
            "thatcherite_chestplate",
            () -> ThatcheriteArmourItem.create(
                    ModArmourMaterials.THATCHERITE,
                    EquipmentSlot.CHEST,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
                            .fireproof()
                            .maxCount(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> THATCHERITE_GREAVES = ITEMS.register(
            "thatcherite_greaves",
            () -> ThatcheriteArmourItem.create(
                    ModArmourMaterials.THATCHERITE,
                    EquipmentSlot.LEGS,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
                            .fireproof()
                            .maxCount(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> THATCHERITE_BOOTS = ITEMS.register(
            "thatcherite_boots",
            () -> ThatcheriteArmourItem.create(
                    ModArmourMaterials.THATCHERITE,
                    EquipmentSlot.FEET,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
                            .fireproof()
                            .maxCount(1)
                            .rarity(Rarity.RARE)
            )
    );

    public static final RegistrySupplier<Item> GRENADE = ITEMS.register(
            "grenade",
            () -> new GrenadeItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .maxCount(16)
            )
    );

    public static final RegistrySupplier<Item> GRENADE_FIRE = ITEMS.register(
            "grenade_fire",
            () -> new FireGrenadeItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .maxCount(12)
            )
    );

    public static final RegistrySupplier<Item> GRENADE_SMOKE = ITEMS.register(
            "grenade_smoke",
            () -> new SmokeGrenadeItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .maxCount(8)
            )
    );

    public static final RegistrySupplier<Item> GRENADE_LAUNCHER = ITEMS.register(
            "grenade_launcher",
            () -> new GrenadeLauncherItem(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .maxCount(1)
                    .maxDamage(512)
                    .maxDamageIfAbsent(512)
            )
    );

    public static final RegistrySupplier<Item> FENCER_SPAWN_EGG = ITEMS.register(
            "fencer_spawn_egg",
            () -> new ArchitecturySpawnEggItem(
                    ModEntities.ROYAL_FENCER,
                    0x000000,
                    0x78582F,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
            )
    );

    public static final RegistrySupplier<Item> GRENADIER_SPAWN_EGG = ITEMS.register(
            "grenadier_spawn_egg",
            () -> new ArchitecturySpawnEggItem(
                    ModEntities.ROYAL_GRENADIER,
                    0x320000,
                    0xAA582F,
                    new Item.Settings()
                            .group(ModItemGroup.THATCHER)
            )
    );

    // Block items

    public static final RegistrySupplier<Item> THATCHERISM_ALTAR = ITEMS.register(
            "thatcherism_altar",
            () -> new BlockItem(ModBlocks.THATCHERISM_ALTAR.get(), new Item.Settings()
                    .group(ModItemGroup.THATCHER)
            )
    );

    // food
    public static final RegistrySupplier<Item> STARGAZY_PIE = ITEMS.register(
            "stargazy_pie",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .rarity(Rarity.COMMON)
                    .food(ModFoodComponents.STARGAZY_PIE)
            )
    );

    public static final RegistrySupplier<Item> FISH_AND_CHIPS = ITEMS.register(
            "fish_and_chips",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .rarity(Rarity.COMMON)
                    .food(ModFoodComponents.FISH_AND_CHIPS)
            )
    );

    public static final RegistrySupplier<Item> CHIP_BUTTY = ITEMS.register(
            "chip_butty",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .rarity(Rarity.COMMON)
                    .food(ModFoodComponents.CHIP_BUTTY)
            )
    );

    public static final RegistrySupplier<Item> STEAK_BAKE = ITEMS.register(
            "steak_bake",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .rarity(Rarity.UNCOMMON)
                    .food(ModFoodComponents.STEAK_BAKE)
            )
    );

    public static final RegistrySupplier<Item> SAUSAGE_ROLL = ITEMS.register(
            "sausage_roll",
            () -> new Item(new Item.Settings()
                    .group(ModItemGroup.THATCHER)
                    .rarity(Rarity.UNCOMMON)
                    .food(ModFoodComponents.SAUSAGE_ROLL)
            )
    );

    public static void registerModItems() {
        ThatcherMod.LOGGER.debug("Registering Items for " + ThatcherMod.MOD_ID + " ...");
        ITEMS.register();
    }
}
