package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class CollieryCloserItem extends PickaxeItem {
    public static final int DURATION = 36000;
    private boolean isBreaking3x3;
    private boolean isFirstClick;

    public CollieryCloserItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
        this.isBreaking3x3 = false;
        this.isFirstClick = true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            convertCoalItems(world, user, hand);
        }
        return super.use(world, user, hand);
    }

    private void convertCoalItems(World world, PlayerEntity user, Hand hand) {
        ItemStack coalStack = user.getOffHandStack();
        boolean success = false;

        if (coalStack.getItem() == Items.COAL) {
            while (!coalStack.isEmpty()) {
                coalStack.decrement(1);

                int random = ThreadLocalRandom.current().nextInt(0, 99 + 1);
                ItemStack oreStack;
                if (0 == random || random == 99) { oreStack = new ItemStack(Items.DIAMOND, 1); } // 2%
                else if (1 <= random && random <= 5) { oreStack = new ItemStack(Items.EMERALD, 1); } // 5%
                else if (6 <= random && random <= 13) { oreStack = new ItemStack(Items.LAPIS_LAZULI, 1); } // 8%
                else if (14 <= random && random <= 23) { oreStack = new ItemStack(Items.GOLD_INGOT, 1); } // 10%
                else if (24 <= random && random <= 38) { oreStack = new ItemStack(Items.QUARTZ, 1); } // 15%
                else if (39 <= random && random <= 53) { oreStack = new ItemStack(Items.REDSTONE, 1); } // 15%
                else if (54 <= random && random <= 73) { oreStack = new ItemStack(Items.COPPER_INGOT, 1); } // 20%
                else { oreStack = new ItemStack(Items.IRON_INGOT, 1); } // 25%

                ItemEntity oreEntity = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), oreStack);
                world.spawnEntity(oreEntity);
                user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand)); // -1 durability
            }
            success = true;
        }

        if (success) {
            CommonMethods.summonDustParticles(world, 3, 1.0f, 0.8255f, 0.26f, 3,
                    user.getX(), user.getY() + 2, user.getZ(), 0, 0, 0);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 12F, 0.66F);
        }
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (this.isBreaking3x3) {
            Iterable<BlockPos> positions = BlockPos.iterate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            for (BlockPos blockPos : positions) {
                Material blockMat = world.getBlockState(blockPos).getMaterial();
                float hardness = world.getBlockState(blockPos).getBlock().getHardness();
                if ((blockMat == Material.STONE || blockMat == Material.METAL) && hardness > 0 && hardness < 30.0F) {
                    world.breakBlock(blockPos, true, miner);

                }
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity miner) {
            Hand hand = miner.getActiveHand();
            if (miner.getStackInHand(hand).getItem() == this) {
                miner.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20));
            }
        }
    }

    @Override
    public boolean isSuitableFor(BlockState state) { return true; }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && this.isFirstClick) { // right click in inventory to switch modes
            if (!this.isBreaking3x3) {
                player.sendMessage(Text.literal("3x3x3 Breaking Mode Activated!"), false);
            } else {
                player.sendMessage(Text.literal("3x3x3 Breaking Mode Disabled!"), false);
            }
            this.isBreaking3x3 = !(this.isBreaking3x3);
            this.isFirstClick = !(this.isFirstClick);
            return true;
        }
        else if (clickType == ClickType.RIGHT && !this.isFirstClick){
            this.isFirstClick = !(this.isFirstClick);
            return true;
        }
        else { return false; }
    }
}
