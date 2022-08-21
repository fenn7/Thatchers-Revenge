package net.fenn7.thatchermod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface NBTInterface {
    static boolean isBombing(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.getBoolean("isBombing");
    }

    static void setBombing(ItemStack stack, boolean bombing) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean("isBombing", bombing);
    }
}
