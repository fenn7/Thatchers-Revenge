package net.fenn7.thatchermod.item.custom.grenade;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.screen.GrenadeLauncherScreenHandler;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.fenn7.thatchermod.item.custom.grenade.GrenadeLauncherInventory.listTagName;
import static net.fenn7.thatchermod.item.custom.grenade.GrenadeLauncherInventory.nbtTagName;

public class GrenadeLauncherItem extends Item {
    private static int COOLDOWN = 30;
    public static String GL_COOLDOWN = "cooldown";
    private GrenadeLauncherInventory grenadeInv;

    public GrenadeLauncherItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack main = user.getMainHandStack();
        if (main.isOf(this)) {
            setInventory(user, Hand.MAIN_HAND);
            boolean shouldrecoil = false;

            if (!user.isSneaking()) {
                user.resetLastAttackedTicks();
                shouldrecoil = shootGrenade(main, this.grenadeInv.getStack(1), world, user);
            } else {
                openScreen(user, user.getStackInHand(hand), this.grenadeInv);
            }

            if (shouldrecoil) {
                IEntityDataSaver data = (IEntityDataSaver) user;
                data.getPersistentData().putBoolean("should_recoil", true);
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int currentCD = nbt.getInt(GL_COOLDOWN);
        if (!world.isClient() && currentCD > 0) {
            nbt.putInt(GL_COOLDOWN, --currentCD);
            ThatcherMod.LOGGER.warn("" + currentCD);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            // uses GrenadeLauncherInventory nbt setup
            NbtCompound nbt = stack.getOrCreateSubNbt(nbtTagName);
            if (nbt.contains(listTagName, 9)) {
                NbtList nbtList = nbt.getList(listTagName, 10);
                for (int i = 0; i < nbtList.size(); i++) {
                    NbtCompound nbtCompound = nbtList.getCompound(i);
                    ItemStack grenadeStack = ItemStack.fromNbt(nbtCompound);
                    if (!grenadeStack.isEmpty()) {
                        tooltip.add(Text.literal( (i + 1) + ": " + grenadeStack.getItem().getTranslationKey() +
                                ": " + grenadeStack.getCount() + "/" + grenadeStack.getMaxCount()));
                    }
                }
            }
        super.appendTooltip(stack, world, tooltip, context);
    }

    private void openScreen(PlayerEntity user, ItemStack stack, GrenadeLauncherInventory grenadeInv) {
        user.world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.HOSTILE, 1.4F, 0.7F);
        user.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
                GrenadeLauncherScreenHandler.createHandler(i, playerInventory, grenadeInv), stack.getName()));
    }

    public boolean shootGrenade(ItemStack thisStack, ItemStack grenadeStack, World world, PlayerEntity user) {
        if (!grenadeStack.isEmpty() && thisStack.getOrCreateNbt().getInt(GL_COOLDOWN) <= 0) {
            AbstractGrenadeItem grenadeItem = (AbstractGrenadeItem) grenadeStack.getItem();
            AbstractGrenadeEntity grenadeEntity = grenadeItem.createGrenadeAt(world, user);
            grenadeEntity.setItem(grenadeStack);
            grenadeEntity.setShouldBounce(false);
            grenadeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2 * grenadeItem.getDefaultSpeed(), 0.0F);
            grenadeEntity.setPower(1.5F * grenadeEntity.getPower());
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.HOSTILE, 1.4F, 0.5F);
            world.spawnEntity(grenadeEntity);
            if (!user.isCreative() && !user.world.isClient()) {
                grenadeStack.decrement(1);
            }
            thisStack.getOrCreateNbt().putInt(GL_COOLDOWN, COOLDOWN);
            markInvDirty();
            return true;
        } else {
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.HOSTILE, 1.4F, 0.5F);
            return false;
        }
    }

    public void setInventory(PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        this.grenadeInv = new GrenadeLauncherInventory(stack);
    }

    private void markInvDirty() {
        this.grenadeInv.markDirty();
    }

    public DefaultedList<ItemStack> getList() {
        return this.grenadeInv.getGrenadeList();
    }

    // unused
    /* private void appendStacktoSlot(int index, ItemStack thisStack, ItemStack otherStack) {
        ItemStack slotStack = this.grenadeInv.getStack(index);
        ItemStack copiedStack = otherStack.copy();
        if (slotStack.isEmpty()) {
            this.grenadeInv.setStack(index, copiedStack);
            otherStack.decrement(copiedStack.getCount());
        }
        else if (ItemStack.canCombine(slotStack, otherStack)) {
            while (otherStack.getCount() > 0 && slotStack.getCount() < slotStack.getMaxCount()) {
                slotStack.increment(1);
                otherStack.decrement(1);
            }
        }
        // maybe try a recursion solution?
        else if (index < this.grenadeInv.getGrenadeList().size() - 1) {
            appendStacktoSlot(index + 1, thisStack, otherStack);
        }
        markInvDirty();
        }*/
}

