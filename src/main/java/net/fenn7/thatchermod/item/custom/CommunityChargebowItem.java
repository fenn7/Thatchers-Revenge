package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CommunityChargebowItem extends BowItem {
    // note most of the arrow effects are handled in PersistentProjectileMixin
    public CommunityChargebowItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            boolean bl = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getArrowType(stack); // most of this is from BowItem
            if (!itemStack.isEmpty() || bl) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }
                int i;
                if (isRapidFiring(stack)) { i = (int) ((0.5 * this.getMaxUseTime(stack))) - remainingUseTicks; } // 2x draw speed in rapid-fire mode, but cannot crit
                else { i = (int) ((1.34 * this.getMaxUseTime(stack)) - remainingUseTicks); } // 34% reduced draw speed, but shot is extremely precise when fully charged
                float f = getPullProgress(i);

                if (!((double) f < 0.1D)) {
                    boolean bl2 = bl && itemStack.isOf(Items.ARROW);
                    if (!world.isClient) {
                        ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                        PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
                        float originalF = getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks);
                        if (isRapidFiring(stack)) {
                            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F * originalF * 3.0F, 1.0F);
                        }
                        else {
                            persistentProjectileEntity.setPos(user.getX(), user.getY() + 1.33D, user.getZ());
                            persistentProjectileEntity.setPierceLevel((byte) 2);
                            if ((this.getMaxUseTime(stack) - remainingUseTicks) >= 27) { // 20 x number of seconds to zone in fully
                                persistentProjectileEntity.setCritical(true); persistentProjectileEntity.setNoGravity(true);
                                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * 1.25);
                                persistentProjectileEntity.setVelocity(user, user.getPitch(), user.headYaw, 0, 5.0F * f, 0.0F);
                                world.playSound(null, playerEntity.getBlockPos(), SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.PLAYERS,  15F, 0.75F);
                            }
                            else {
                                persistentProjectileEntity.setVelocity(user, user.getPitch(), user.headYaw, 0, originalF * 2.0F, 0.0F);
                            }
                        }

                        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (j > 0) { persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double) j * 0.5D + 0.5D); }

                        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (k > 0) { persistentProjectileEntity.setPunch(k); }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) { persistentProjectileEntity.setOnFireFor(100); }

                        stack.damage(1, playerEntity, (p) -> p.sendToolBreakStatus(playerEntity.getActiveHand()));

                        if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }
                        world.spawnEntity(persistentProjectileEntity);
                    }
                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            playerEntity.getInventory().removeOne(itemStack);
                        }
                    }
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            boolean success = false;
            List<Entity> nearbyEntities = CommonMethods.getEntitiesNearPlayer(user, 5, 4, 5, -5, -4, -5, world); {
                for (Entity entity: nearbyEntities) {
                    if (entity instanceof LivingEntity alive && entity != user) {
                        if (!world.isClient) { CommonMethods.summonDustParticles(world, 1, 0f, 0.33f, 0f, 2,
                                    alive.getX(), alive.getY() + 0.5D, alive.getZ(), 0, 0, 0); }
                        alive.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1));
                        alive.setAttacker(user); alive.setAttacking(user); // aggros mobs
                        user.getMainHandStack().damage(1, user, (p) -> p.sendToolBreakStatus(hand)); // -1 durability for each
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 150, 1));
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 150,
                                (int) (nearbyEntities.size()/4)), user); // every 4 entities hit grants +1 level of speed
                        success = true;
                    }
                }
            }
            if (success && !world.isClient) {
                CommonMethods.summonDustParticles(world, 1, 0.0f, 0.33f, 0.0f, 3,
                        user.getX(), user.getY() + 2, user.getZ(), 0, 0, 0);
                world.playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 45F, 1.5F);
                return TypedActionResult.fail(this.getDefaultStack());
            }
            else { return super.use(world, user, hand); }
        }
        else { return super.use(world, user, hand); }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT) { // right click in inventory to switch modes
            if (!isRapidFiring(stack)) {
                player.sendMessage(Text.literal("Rapid-Fire Mode Activated!"), true);
                setRapidFiring(stack, true);
            } else {
                player.sendMessage(Text.literal("Precision Mode Activated!"), true);
                setRapidFiring(stack, false);
            }
            return true;
        }
        else { return false; }
    }

    public static boolean isRapidFiring(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.getBoolean("rapid.fire");
    }

    public static void setRapidFiring(ItemStack stack, boolean active) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean("rapid.fire", active);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Ability Ready!"));
        } else {
            tooltip.add(Text.literal("Use + Sneak to escape from enemies"));
            if (isRapidFiring(stack)) {
                tooltip.add(Text.literal("Currently in Rapid-Fire Mode"));
            }
            else {
                tooltip.add(Text.literal("Currently in Precision Mode"));
            }
        }
    }
}
