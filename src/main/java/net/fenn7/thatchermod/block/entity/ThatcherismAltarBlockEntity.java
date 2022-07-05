package net.fenn7.thatchermod.block.entity;

import net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.inventory.ImplementedInventory;
import net.fenn7.thatchermod.screen.ThatcherismAltarScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import oshi.hardware.SoundCard;

import java.util.concurrent.ThreadLocalRandom;

import static net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock.IS_CHANNELING;
import static net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock.IS_PREPARED;

public class ThatcherismAltarBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private String displayName = "§fᔑ ʖ⚍∷リ╎リ⊣ ᓭJ⚍ꖎ, ᔑ ⎓∷J⨅ᒷリ ⍑ᒷᔑ∷ℸ";

    /* USED FOR ANIMATED HELLFIRE BLOOD BAR */
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 28;
    private int channelingProgress = 0;
    private int maxChannelingProgress = 200;

    public ThatcherismAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.THATCHERISM_ALTAR, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return progress;
                    case 1: return maxProgress;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: ThatcherismAltarBlockEntity.this.progress = value; break;
                    case 1: ThatcherismAltarBlockEntity.this.maxProgress = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal(displayName);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ThatcherismAltarScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("altar.progress", progress);
        nbt.putInt("channeling.progress", channelingProgress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        nbt.getInt("altar.progress");
        nbt.getInt("channeling.progress");
    }

        public static void tick(World world, BlockPos pos, BlockState state, ThatcherismAltarBlockEntity entity) {

        if (state.get(IS_PREPARED) && !state.get(IS_CHANNELING)) {
            double diff1 = ThreadLocalRandom.current().nextDouble(-0.33D, 0.33D);
            double diff2 = ThreadLocalRandom.current().nextDouble(-0.33D, 0.33D);
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, 0, 0, 0);
            world.addParticle(ParticleTypes.SOUL, pos.getX() + 0.5D + diff1, pos.getY() + 1.5D, pos.getZ() + 0.5D + diff2, 0, 0, 0);
        }

        if (state.get(IS_CHANNELING)) {
            emptyAltar(entity);
            world.setBlockState(pos, state.with(IS_PREPARED, false));
            if (entity.channelingProgress < entity.maxChannelingProgress) {
                entity.channelingProgress++;
                if (entity.channelingProgress % 20 == 0){
                    world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0, 0);
                    world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0, 0);
                };
                if (entity.channelingProgress == entity.maxChannelingProgress) {
                    world.setBlockState(pos, state.with(IS_CHANNELING, false));
                    entity.channelingProgress = 0;
                }
            }
        }

        if (hasRecipe(entity)) {
            if (entity.progress < entity.maxProgress) {
                entity.progress++;
                if (entity.progress == entity.maxProgress) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 5F, 1F);
                    ThatcherismAltarBlock.setIsPreparedTrue(world, pos, state);
                }
            }
        }
        else {
            if (entity.progress > 0) {
                entity.progress--;
                if (entity.progress != entity.maxProgress) {
                    ThatcherismAltarBlock.setIsPreparedFalse(world, pos, state);
                }
            }
        }
    }

    private static boolean hasRecipe(ThatcherismAltarBlockEntity entity) {
        boolean hasRecipe = false;
        if ((entity.getStack(0).getItem() == ModItems.HEART_OF_THATCHER &&
                entity.getStack(1).getItem() == ModItems.SOUL_OF_THATCHER) ||
                (entity.getStack(0).getItem() == ModItems.SOUL_OF_THATCHER &&
                entity.getStack(1).getItem() == ModItems.HEART_OF_THATCHER)) {

            hasRecipe = true;
        }
        return hasRecipe;
    }

    public static void emptyAltar(ThatcherismAltarBlockEntity entity) {
        entity.setStack(0, ItemStack.EMPTY);
        entity.setStack(1, ItemStack.EMPTY);
    }
}

