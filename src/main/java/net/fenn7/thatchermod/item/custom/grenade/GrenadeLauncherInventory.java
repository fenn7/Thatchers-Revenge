package net.fenn7.thatchermod.item.custom.grenade;

import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.item.inventory.ImplementedInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

/*public class GrenadeLauncherInventory extends SimpleInventory implements Inventory {
    private final DefaultedList<ItemStack> list;
    private final ItemStack stack;
    private GrenadeNBTSaver saver;

    public GrenadeLauncherInventory(ItemStack stack, DefaultedList<ItemStack> list) {
        this.stack = stack;
        this.list = list;
        this.saver = GrenadeNBTSaver.EMPTY;
    }

    public ItemStack returnFinalStack() {
        return stack;
    }

    public GrenadeNBTSaver saver() {
        return this.saver;
    }

    public void setSaver(GrenadeNBTSaver saver) {
        this.saver = saver;
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.list) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.list.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.list, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        final ItemStack stack = this.list.remove(slot);
        this.setStack(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.list.set(slot, stack);
    }

    @Override
    public void markDirty() {
        this.saver.write(this.list);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.list.size(); i++) {
            this.removeStack(i);
        }
    }

    @Override
    public void onOpen(PlayerEntity player) {
        this.saver.read(this.list);
    }

    public DefaultedList<ItemStack> list() {
        return this.list;
    }
}*/

public class GrenadeLauncherInventory implements ImplementedInventory
{
    protected static final String nbtTagName = "Grenades";
    protected static final String listTagName = "Items.List";
    private final ItemStack stack;
    private final DefaultedList<ItemStack> grenadeList = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public GrenadeLauncherInventory(ItemStack stack)
    {
        this.stack = stack;
        NbtCompound nbt = stack.getSubNbt(nbtTagName);
        if (nbt != null) {
            Inventories.readNbt(nbt, grenadeList);
            writeItemsAsNBTList();
        }
    }

    @Override
    public DefaultedList<ItemStack> getGrenadeList() {
        return grenadeList;
    }

    @Override
    public void markDirty() {
        NbtCompound nbt = stack.getOrCreateSubNbt(nbtTagName);
        Inventories.writeNbt(nbt, grenadeList);
        writeItemsAsNBTList();
    }

    private void writeItemsAsNBTList() {
        NbtCompound nbt = stack.getSubNbt(nbtTagName);
        List<ItemStack> stackList = new ArrayList<>(this.grenadeList);
        if (!stackList.isEmpty()) {
            NbtList nbtList = new NbtList();
            for (ItemStack itemStack : stackList) {
                nbtList.add(itemStack.writeNbt(new NbtCompound()));
            }
            nbt.put(listTagName, nbtList);
        }
    }
}
