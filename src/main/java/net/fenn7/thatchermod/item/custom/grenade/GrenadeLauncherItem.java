package net.fenn7.thatchermod.item.custom.grenade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.fenn7.thatchermod.screen.GrenadeLauncherScreenHandler;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrenadeLauncherItem extends Item {
    private final DefaultedList<ItemStack> list = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final String nbtTagName = "Grenades";

    public GrenadeLauncherItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!player.world.isClient()) {
            if (clickType == ClickType.RIGHT) {
                openScreen(player, stack);
                return true;
            }
            if (clickType == ClickType.LEFT && otherStack.getItem() instanceof AbstractGrenadeItem) {
                int index = this.list.get(0).isEmpty() ? 0 : 1;
                appendStacktoSlot(index, stack, otherStack);
                return true;
            }
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (!user.isSneaking()) {
                openScreen(user, user.getStackInHand(hand));
            }
            else {
                ItemStack grenadeStack = list.get(0);
                AbstractGrenadeItem grenadeItem = (AbstractGrenadeItem) grenadeStack.getItem();

                AbstractGrenadeEntity grenadeEntity = grenadeItem.createGrenadeAt(world, user);
                grenadeEntity.setItem(grenadeStack);
                grenadeEntity.setShouldBounce(false);
                grenadeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2 * grenadeItem.getDefaultSpeed(), 0.0F);
                grenadeEntity.setPower(1.5F * grenadeEntity.getPower());
                world.spawnEntity(grenadeEntity);

                if (!user.isCreative()) {
                    grenadeStack.decrement(1);
                }

                saveListNBT(user.getStackInHand(hand));
            }
        }
        IEntityDataSaver data = (IEntityDataSaver) user;
        data.getPersistentData().putBoolean("should_recoil", true);
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ItemStack slot0 = this.list.get(0);
        ItemStack slot1 = this.list.get(1);
        if (!slot0.isEmpty()) {
            tooltip.add(Text.literal(slot0.getItem().getTranslationKey() + ": " + slot0.getCount() + "/" + slot0.getMaxCount()));
        }
        if (!slot1.isEmpty()) {
            tooltip.add(Text.literal(slot1.getItem().getTranslationKey() + ": " + slot1.getCount() + "/" + slot1.getMaxCount()));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    private void openScreen(PlayerEntity user, ItemStack stack) {
        user.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return stack.getName();
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new GrenadeLauncherScreenHandler(syncId, inv, new GrenadeLauncherInventory(stack, list));
            }
        });
    }

    private void appendStacktoSlot(int index, ItemStack thisStack, ItemStack otherStack) {
        ItemStack slotStack = this.list.get(index);
        ItemStack copiedStack = otherStack.copy();
        if (slotStack.isEmpty()) {
            list.set(0, copiedStack);
            otherStack.decrement(copiedStack.getCount());
        }
        else if (ItemStack.canCombine(slotStack, otherStack)) {
            while (slotStack.getCount() < copiedStack.getCount() && slotStack.getCount() < slotStack.getMaxCount()) {
                slotStack.increment(1);
                otherStack.decrement(1);
            }
        }
        saveListNBT(thisStack);
    }

    private void saveListNBT(ItemStack stack){
        NbtCompound nbt = new NbtCompound();
        Inventories.writeNbt(new NbtCompound(), list, true);

        Inventories.writeNbt(nbt, list, true);
        stack.getOrCreateNbt().put(nbtTagName, nbt);
    }
}
