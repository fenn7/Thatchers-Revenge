package net.fenn7.thatchermod.item.custom.grenade;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface GrenadeNBTSaver {
    GrenadeNBTSaver EMPTY = new GrenadeNBTSaver() {
        @Override
        public void write(DefaultedList<ItemStack> stacks) {
            // Empty
        }

        @Override
        public void read(DefaultedList<ItemStack> stacks) {
            // Empty
        }
    };

    void write(DefaultedList<ItemStack> stacks);

    void read(DefaultedList<ItemStack> stacks);
}