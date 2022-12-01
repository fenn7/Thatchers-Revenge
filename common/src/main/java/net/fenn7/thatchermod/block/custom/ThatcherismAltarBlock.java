package net.fenn7.thatchermod.block.custom;

import net.fenn7.thatchermod.block.ModBlockEntities;
import net.fenn7.thatchermod.block.blockentity.ThatcherismAltarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class ThatcherismAltarBlock extends BlockWithEntity implements BlockEntityProvider {

    public static BooleanProperty IS_PREPARED = BooleanProperty.of("is_prepared");
    public static BooleanProperty IS_CHANNELING = BooleanProperty.of("is_channeling");

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_PREPARED);
        builder.add(IS_CHANNELING);
    }

    public ThatcherismAltarBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(IS_PREPARED, false));
        setDefaultState(this.getDefaultState().with(IS_CHANNELING, false));
    }

    /* Block Entity Stuff */

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThatcherismAltarBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ThatcherismAltarBlockEntity) {
                ItemScatterer.spawn(world, pos, (ThatcherismAltarBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && !state.get(IS_CHANNELING)) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null && player.getMainHandStack().getItem() != (Items.DIAMOND)
                    || screenHandlerFactory != null && !state.get(IS_PREPARED)) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }

        if (state.get(IS_PREPARED) && player.getMainHandStack().getItem() == (Items.DIAMOND)) {
            player.getMainHandStack().decrement(1);
            world.setBlockState(pos, state.with(IS_CHANNELING, true));
            world.playSound(null, pos, new SoundEvent(new Identifier("thatchermod:thatcher_summoning")),
                    SoundCategory.BLOCKS, 4F, 1F);
            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN,
                    SoundCategory.BLOCKS, 5F, 0.5F);
        }

        return ActionResult.SUCCESS;
    }

    public static void setIsPreparedTrue(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(IS_PREPARED, true));
    }

    public static void setIsPreparedFalse(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(IS_PREPARED, false));
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(IS_PREPARED) ? 15 : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.THATCHERISM_ALTAR.get(), ThatcherismAltarBlockEntity::tick);
    }
}
