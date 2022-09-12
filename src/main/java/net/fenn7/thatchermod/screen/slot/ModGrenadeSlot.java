package net.fenn7.thatchermod.screen.slot;

import net.fenn7.thatchermod.item.custom.grenade.AbstractGrenadeItem;
import net.fenn7.thatchermod.item.custom.grenade.GrenadeItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ModGrenadeSlot extends Slot {
    public ModGrenadeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) { return stack.getItem() instanceof AbstractGrenadeItem; }
}
