package net.fenn7.thatchermod.commonside.screen.slot;

import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.ThatcherSoulItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ModThatcherSlot extends Slot {
    public ModThatcherSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ThatcherSoulItem || stack.isOf(ModItems.HEART_OF_THATCHER.get());
    }
}
