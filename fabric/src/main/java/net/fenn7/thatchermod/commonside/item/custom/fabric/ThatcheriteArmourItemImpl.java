package net.fenn7.thatchermod.commonside.item.custom.fabric;

import net.fenn7.thatchermod.commonside.item.custom.ThatcheriteArmourItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;

public class ThatcheriteArmourItemImpl {

    public static Item create(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
        return new ThatcheriteArmourItem(material, slot, settings);
    }
}
