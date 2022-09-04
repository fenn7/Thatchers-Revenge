package net.fenn7.thatchermod.item.custom.grenade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

import java.util.UUID;

public class GrenadeLauncherInventory extends SimpleInventory implements Inventory {
    private final DefaultedList<ItemStack> list = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private Hand hand;
    private final ItemStack stack;
    private UUID uuid;
    private GrenadeNBTSaver saver;

    public GrenadeLauncherInventory(ItemStack stack) {
        this.stack = stack;
        //this.uuid = uuid;
        this.saver = GrenadeNBTSaver.EMPTY;
    }

    public ItemStack returnFinalStack() {
        return stack;
    }

    public Hand hand() {
        return this.hand;
    }

    public UUID uuid() {
        return this.uuid;
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
}
