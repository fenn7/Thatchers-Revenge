package net.fenn7.thatchermod.screen.slot;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.ThatcherSoulItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.tag.ItemTags;

public class ModThatcherSlot extends Slot {
    public ModThatcherSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ThatcherSoulItem || stack.isOf(ModItems.HEART_OF_THATCHER);
    }
}
