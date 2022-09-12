package net.fenn7.thatchermod.item.custom.grenade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.fenn7.thatchermod.screen.GrenadeLauncherScreenHandler;
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
        if (clickType == ClickType.RIGHT) {
            openScreen(player, stack);
            ThatcherMod.LOGGER.warn("aba");
            return true;
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (list.get(0).getCount() <= 0) {
                openScreen(user, user.getStackInHand(hand));
            }
            else if (!user.isSneaking()) {
                ItemStack grenadeStack = list.get(0);
                AbstractGrenadeItem grenadeItem = (AbstractGrenadeItem) grenadeStack.getItem();

                AbstractGrenadeEntity grenadeEntity = grenadeItem.createGrenadeAt(world, user);
                grenadeEntity.setItem(grenadeStack);
                grenadeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2.5F, 0.0F);
                grenadeEntity.setPower(1.5F * grenadeEntity.getPower());
                world.spawnEntity(grenadeEntity);

                if (!user.isCreative()) {
                    grenadeStack.decrement(1);
                }

                saveListNBT(user, hand);
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public void openScreen(PlayerEntity user, ItemStack stack) {
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

    private void saveListNBT(PlayerEntity player, Hand hand){
        NbtCompound nbt = new NbtCompound();
        ItemStack stack = player.getStackInHand(hand);
        Inventories.writeNbt(new NbtCompound(), list, true);

        Inventories.writeNbt(nbt, list, true);
        stack.getOrCreateNbt().put(nbtTagName, nbt);
    }
}
