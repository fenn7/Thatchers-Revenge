package net.fenn7.thatchermod.block.entity;

import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.inventory.ImplementedInventory;
import net.fenn7.thatchermod.screen.ThatcherismAltarScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ThatcherismAltarBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public ThatcherismAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.THATCHERISM_ALTAR, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("§fᔑ ʖ⚍∷リ╎リ⊣ ᓭJ⚍ꖎ, ᔑ ⎓∷J⨅ᒷリ ⍑ᒷᔑ∷ℸ");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ThatcherismAltarScreenHandler(syncId, inv, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
    }

        public static void tick(World world, BlockPos pos, BlockState state, ThatcherismAltarBlockEntity entity) {
        if(hasRecipe(entity) && hasNotReachedStackLimit(entity)) {
            craftItem(entity);
        }
    }

    private static void craftItem(ThatcherismAltarBlockEntity entity) {
        MinecraftClient.getInstance().player.sendChatMessage("Bruh");
    }

    private static boolean hasRecipe(ThatcherismAltarBlockEntity entity) {
        boolean hasItemInFirstSlot = entity.getStack(0).getItem() == ModItems.HEART_OF_THATCHER;
        boolean hasItemInSecondSlot = entity.getStack(1).getItem() == ModItems.SOUL_OF_THATCHER;

        return hasItemInFirstSlot && hasItemInSecondSlot;
    }

    private static boolean hasNotReachedStackLimit(ThatcherismAltarBlockEntity entity) {
        return true;
    }
}

