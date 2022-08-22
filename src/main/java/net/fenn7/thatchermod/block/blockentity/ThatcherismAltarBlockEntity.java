package net.fenn7.thatchermod.block.blockentity;

import net.fenn7.thatchermod.block.ModBlockEntities;
import net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.inventory.ImplementedInventory;
import net.fenn7.thatchermod.screen.ThatcherismAltarScreenHandler;
import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock.IS_CHANNELING;
import static net.fenn7.thatchermod.block.custom.ThatcherismAltarBlock.IS_PREPARED;

public class ThatcherismAltarBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final String displayName = "§fᔑ ʖ⚍∷リ╎リ⊣ ᓭJ⚍ꖎ, ᔑ ⎓∷J⨅ᒷリ ⍑ᒷᔑ∷ℸ";

    protected final PropertyDelegate propertyDelegate;
    private List<BlockPos> positions;
    private int progress = 0;
    private int maxProgress = 28;
    private int channelingProgress = 0;
    private int maxChannelingProgress = 179;

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

    public void onOpen(PlayerEntity player) {
        this.positions = buildCircleStrikeList(player, pos);
        ImplementedInventory.super.onOpen(player);
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
                if (entity.channelingProgress % 20 == 0 && entity.channelingProgress <= 140) {
                    if (!world.isClient() && !entity.positions.isEmpty()) { // at 20TPS this will occur every second
                        BlockPos strikePos = entity.positions.get(entity.channelingProgress / 20);
                        world.addParticle(ParticleTypes.LARGE_SMOKE, strikePos.getX(), strikePos.getY(), strikePos.getZ(), 0, 2, 0);
                        EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, null, null, null,
                                strikePos, SpawnReason.TRIGGERED, true, true);
                        extinguishFire(strikePos, world);
                    }
                    world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0, 0);
                    world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0, 0);
                }
                if (entity.channelingProgress == 170) { //should do this on the final major beat of the song
                    if (!world.isClient()) {
                        EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, null, null, null, pos,
                                SpawnReason.TRIGGERED, true, true);
                        ModEntities.THATCHER.spawn((ServerWorld) world, null, null, null, pos,
                                SpawnReason.MOB_SUMMONED, true, true);
                    }
                }
                entity.channelingProgress++;
                if (entity.channelingProgress == entity.maxChannelingProgress) { //reset states
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
        } else {
            if (entity.progress > 0) {
                entity.progress--;
                if (entity.progress != entity.maxProgress) {
                    ThatcherismAltarBlock.setIsPreparedFalse(world, pos, state);
                }
            }
        }
    }

    private List<BlockPos> buildCircleStrikeList(PlayerEntity player, BlockPos pos) {
        //get 8 block positions in a circle around the altar's position.
        BlockPos N = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 4);
        BlockPos NE = new BlockPos(pos.getX() + 3, pos.getY(), pos.getZ() - 3);
        BlockPos E = new BlockPos(pos.getX() + 4, pos.getY(), pos.getZ());
        BlockPos SE = new BlockPos(pos.getX() + 3, pos.getY(), pos.getZ() + 3);
        BlockPos S = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 4);
        BlockPos SW = new BlockPos(pos.getX() - 3, pos.getY(), pos.getZ() + 3);
        BlockPos W = new BlockPos(pos.getX() - 4, pos.getY(), pos.getZ());
        BlockPos NW = new BlockPos(pos.getX() - 3, pos.getY(), pos.getZ() - 3);

        Direction direction = player.getMovementDirection();
        List<BlockPos> posList;
        switch (direction) {
            default -> { posList = Arrays.asList(N, NE, E, SE, S, SW, W, NW); break; }
            case EAST -> { posList = Arrays.asList(E, SE, S, SW, W, NW, N, NE); break; }
            case SOUTH -> { posList = Arrays.asList(S, SW, W, NW, N, NE, E, SE); break; }
            case WEST -> { posList = Arrays.asList(W, NW, N, NE, E, SE, S, SW); break;
            }
        }

        if (player.world != null) {
            for (int i = 0; i < posList.size(); i++) {
                posList.set(i, CommonMethods.findFirstNonAirBlockDown(player.world, posList.get(i)).offset(Direction.UP, 1));
            }
        }
        return posList;
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

    private static void extinguishFire(BlockPos pos, World world) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isIn(BlockTags.FIRE)) {
            world.removeBlock(pos, false);
        }
    }
}
