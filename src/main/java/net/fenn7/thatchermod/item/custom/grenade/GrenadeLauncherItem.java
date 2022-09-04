package net.fenn7.thatchermod.item.custom.grenade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.screen.GrenadeLauncherScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GrenadeLauncherItem extends Item {

    public GrenadeLauncherItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT) {
            openScreen(player, player.getActiveHand());
            ThatcherMod.LOGGER.warn("aba");
            return true;
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            openScreen(user, hand);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public void openScreen(PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        GrenadeLauncherItem gl = (GrenadeLauncherItem) stack.getItem();

        user.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return stack.getName();
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new GrenadeLauncherScreenHandler(syncId, inv, new GrenadeLauncherInventory(hand));
            }
        });
    }
}
