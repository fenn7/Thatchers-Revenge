package net.fenn7.thatchermod.enchantments;

import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {

    public static Enchantment BAILOUT = register("bailout",
            new BailoutEnchantment(Enchantment.Rarity.VERY_RARE,
                    EnchantmentTarget.VANISHABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment TORPEDO = register("torpedo",
            new TorpedoEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment AIR_ASSAULT = register("air_assault",
            new AirAssaultEnchantment(Enchantment.Rarity.VERY_RARE,
                    EnchantmentTarget.WEARABLE, new EquipmentSlot[]{EquipmentSlot.CHEST}));
    public static Enchantment PRIVATISATION = register("privatisation",
            new PrivatisationEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment AMPHIBIOUS = register("amphibious",
            new AmphibiousEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static Enchantment CURSED_GUARD = register("cursed_guard",
            new CursedGuardEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST}));
    public static Enchantment DISCONTENT = register("discontent",
            new DiscontentEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS}));
    public static Enchantment HUNGERING_STRIKE = register("hungering_strike",
            new HungeringStrikeEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(ThatcherMod.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments() {
        System.out.println("Registering Enchantments for " + ThatcherMod.MOD_ID);
    }
}
