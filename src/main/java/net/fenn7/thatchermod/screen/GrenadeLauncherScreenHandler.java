package net.fenn7.thatchermod.screen;

import net.fenn7.thatchermod.item.custom.grenade.GrenadeLauncherInventory;
import net.fenn7.thatchermod.item.custom.grenade.GrenadeNBTSaver;
import net.fenn7.thatchermod.screen.slot.ModGrenadeSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;

public class GrenadeLauncherScreenHandler extends ScreenHandler implements GrenadeNBTSaver {
    private final GrenadeLauncherInventory grenadeInv;
    private final PlayerInventory playerInv;
    private final String nbtTagName = "Grenades";

    public GrenadeLauncherScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new GrenadeLauncherInventory(playerInventory.player.getMainHandStack()));
    }

    public GrenadeLauncherScreenHandler(int syncId, PlayerInventory playerInventory, GrenadeLauncherInventory inventory) {
        super(ModScreenHandlers.GRENADE_LAUNCHER_SCREEN_HANDLER, syncId);
        checkSize(inventory, 2);
        this.grenadeInv = inventory;
        this.playerInv = playerInventory;

        if (!playerInv.player.getWorld().isClient()) {
            this.grenadeInv.setSaver(this);
        }

        this.addSlot(new ModGrenadeSlot(inventory, 0, 34, 41));
        this.addSlot(new ModGrenadeSlot(inventory, 1, 126, 41));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
        inventory.onOpen(playerInventory.player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 77 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 135));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack stack2 = slot.getStack();
            stack = stack2.copy();
            if (index < this.grenadeInv.size()) {
                if (!this.insertItem(stack2, this.grenadeInv.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stack2, 0, this.grenadeInv.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return stack;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType type, PlayerEntity player) {
        if (slotIndex >= 0 && player.getInventory().selectedSlot + 27 + this.grenadeInv.size() == slotIndex) {
            if (type != SlotActionType.CLONE) {
                return;
            }
        }
        super.onSlotClick(slotIndex, button, type, player);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.grenadeInv.onClose(player);
    }

    public GrenadeLauncherInventory inventory() {
        return this.grenadeInv;
    }

    @Override
    public void write(DefaultedList<ItemStack> stacks) {
        ItemStack stack = this.grenadeInv.returnFinalStack();
        NbtCompound nbt = new NbtCompound();
        Inventories.writeNbt(nbt, stacks, true);
        stack.getOrCreateNbt().put(nbtTagName, nbt);
    }

    @Override
    public void read(DefaultedList<ItemStack> stacks) {
        ItemStack stack = this.grenadeInv.returnFinalStack();
        Inventories.readNbt(stack.getOrCreateNbt().getCompound(nbtTagName), stacks);
    }
}
