package net.fenn7.thatchermod.item.custom;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fenn7.thatchermod.item.ModToolMaterials;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

public class CommunityCrossbowItem extends CrossbowItem {

    public CommunityCrossbowItem(Settings settings) {
        super(settings);
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.kill();
        return super.postHit(stack, target, attacker);
    }
}
