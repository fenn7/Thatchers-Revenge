package net.fenn7.thatchermod.commonside.enchantments;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {

    private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.ENCHANTMENT_KEY);

    public static RegistrySupplier<Enchantment> BAILOUT = ENCHANTMENTS.register(
            "bailout",
            () -> new BailoutEnchantment(
                    Enchantment.Rarity.VERY_RARE,
                    EnchantmentTarget.VANISHABLE,
                    new EquipmentSlot[]{EquipmentSlot.MAINHAND}
            )
    );

    public static RegistrySupplier<Enchantment> TORPEDO = ENCHANTMENTS.register(
            "torpedo",
            () -> new TorpedoEnchantment(
                    Enchantment.Rarity.RARE,
                    EnchantmentTarget.TRIDENT,
                    new EquipmentSlot[]{EquipmentSlot.MAINHAND}
            )
    );

    public static RegistrySupplier<Enchantment> AIR_ASSAULT = ENCHANTMENTS.register(
            "air_assault",
            () -> new AirAssaultEnchantment(
                    Enchantment.Rarity.VERY_RARE,
                    EnchantmentTarget.WEARABLE,
                    new EquipmentSlot[]{EquipmentSlot.CHEST}
            )
    );

    public static RegistrySupplier<Enchantment> INTERCEPTOR = ENCHANTMENTS.register(
            "interceptor",
            () -> new InterceptorEnchantment(
                    Enchantment.Rarity.RARE,
                    EnchantmentTarget.WEARABLE,
                    new EquipmentSlot[]{EquipmentSlot.CHEST}
            )
    );

    public static RegistrySupplier<Enchantment> STEALTH = ENCHANTMENTS.register(
            "stealth",
            () -> new StealthEnchantment(
                    Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.WEARABLE,
                    new EquipmentSlot[]{EquipmentSlot.CHEST}
            )
    );

    public static RegistrySupplier<Enchantment> JET_ASSIST = ENCHANTMENTS.register(
            "jet_assist",
            () -> new JetAssistEnchantment(
                    Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEARABLE,
                    new EquipmentSlot[]{EquipmentSlot.CHEST}
            )
    );

    public static RegistrySupplier<Enchantment> PRIVATISATION = ENCHANTMENTS.register(
            "privatisation",
            () -> new PrivatisationEnchantment(
                    Enchantment.Rarity.COMMON,
                    EnchantmentTarget.DIGGER,
                    new EquipmentSlot[]{EquipmentSlot.MAINHAND}
            )
    );

    public static RegistrySupplier<Enchantment> AMPHIBIOUS = ENCHANTMENTS.register(
            "amphibious",
            () -> new AmphibiousEnchantment(
                    Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON,
                    new EquipmentSlot[]{EquipmentSlot.MAINHAND}
            )
    );

    public static RegistrySupplier<Enchantment> CURSED_GUARD = ENCHANTMENTS.register(
            "cursed_guard",
            () -> new CursedGuardEnchantment(
                    Enchantment.Rarity.COMMON,
                    EnchantmentTarget.ARMOR_CHEST,
                    new EquipmentSlot[]{EquipmentSlot.CHEST}
            )
    );

    public static RegistrySupplier<Enchantment> DISCONTENT = ENCHANTMENTS.register(
            "discontent",
            () -> new DiscontentEnchantment(
                    Enchantment.Rarity.COMMON,
                    EnchantmentTarget.ARMOR_LEGS,
                    new EquipmentSlot[]{EquipmentSlot.LEGS}
            )
    );

    public static RegistrySupplier<Enchantment> HUNGERING_STRIKE = ENCHANTMENTS.register(
            "hungering_strike",
            () -> new HungeringStrikeEnchantment(
                    Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON,
                    new EquipmentSlot[]{EquipmentSlot.MAINHAND}
            )
    );

    public static void registerModEnchantments() {
        System.out.println("Registering Enchantments for " + ThatcherMod.MOD_ID);
        ENCHANTMENTS.register();
    }
}
