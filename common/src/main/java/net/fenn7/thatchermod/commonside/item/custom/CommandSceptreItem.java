package net.fenn7.thatchermod.commonside.item.custom;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.enchantments.ModEnchantments;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.entity.projectiles.CursedMeteorEntity;
import net.fenn7.thatchermod.commonside.entity.projectiles.CursedMissileEntity;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.util.CommonMethods;
import net.fenn7.thatchermod.commonside.util.ThatcherModEntityData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class CommandSceptreItem extends Item {
    private static List<StatusEffect> BENEFICIAL_EFFS;
    private static final int COOLDOWN = 120;
    private static final double RANGE = 20;
    private static String CS_COOLDOWN = "cooldown";
    private int ticks;

    public CommandSceptreItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isCreative() || user.getInventory().count(Items.LAPIS_LAZULI) >= 4) {
            if (!world.isClient() && hand == Hand.MAIN_HAND) {
                if (user.getMainHandStack().getOrCreateNbt().getInt(CS_COOLDOWN) <= 0) {
                    CommonMethods.summonDustParticles(world, 1, 0.0F, 0.0F, 0.33F, 3,
                            user.getX(), user.getY() + 2, user.getZ(), 0, 0, 0);
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_SKELETON_HORSE_DEATH, SoundCategory.HOSTILE, 5F, 0.75F);
                    summonMeteorEntity(world, user);
                    user.getMainHandStack().damage(3, user, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                    user.getMainHandStack().getOrCreateNbt().putInt(CS_COOLDOWN, COOLDOWN);
                } else {
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.HOSTILE, 2.5F, 0.5F);
                }
            }
        }
        return super.use(world, user, hand);
    }

    public void launchMissileEntity(World world, PlayerEntity user) {
        CursedMissileEntity missileEntity = new CursedMissileEntity(ModEntities.CURSED_MISSILE.get(), world);
        missileEntity.setOwner(user);
        missileEntity.setPos(user.getX(), user.getBodyY(0.7D), user.getZ());
        missileEntity.setVelocity(user, user.getPitch(), user.headYaw, 0, 4.0F, 0.25F);
        world.spawnEntity(missileEntity);

        if (!user.isCreative() && !world.isClient) {
            int lapisSlot = getLapisSlotInv(user.getInventory());
            if (lapisSlot != -1337) {
                user.getInventory().getStack(lapisSlot).decrement(1);
            }
        }
    }

    private void summonMeteorEntity(World world, PlayerEntity user) {
        CursedMeteorEntity meteorEntity = new CursedMeteorEntity(ModEntities.CURSED_METEOR.get(), world);
        meteorEntity.setOwner(user);
        meteorEntity.setFalling(true);
        BlockPos impactPos = findPosPlayerLookingAt(user).offset(Direction.UP);
        CommonMethods.summonDustParticles(world, 10, 0, 0, 0.33F, 2,
                impactPos.getX() + 0.5D, impactPos.getY() + 0.5D, impactPos.getZ() + 0.5D, 0, 0, 0);
        meteorEntity.setLowestNoClipY(1.0 + impactPos.getY());
        meteorEntity.setPos(impactPos.getX() + 0.5, impactPos.getY() + 17, impactPos.getZ() + 0.5);
        world.spawnEntity(meteorEntity);

        if (!user.isCreative() && !world.isClient) {
            int lapisSlot = getLapisSlotInv(user.getInventory());
            if (lapisSlot != -1337) {
                user.getInventory().getStack(lapisSlot).decrement(4);
            }
        }
    }

    private BlockPos findPosPlayerLookingAt(PlayerEntity user) {
        // sight detection
        Vec3d eyes = user.getEyePos();
        Vec3d linearSight = user.getRotationVec(1.0F).normalize().multiply(20);
        var result = ProjectileUtil.getEntityCollision(user.world, user, eyes, eyes.add(linearSight),
                new Box(user.getBlockPos()).expand(20), a -> true, 0.5F);
        return result != null ? result.getEntity().getBlockPos() : new BlockPos(user.raycast(RANGE, 0, true).getPos());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            NbtCompound nbt = stack.getOrCreateNbt();
            int currentCD = nbt.getInt(CS_COOLDOWN);
            if (!world.isClient() && currentCD > 0) {
                nbt.putInt(CS_COOLDOWN, --currentCD);
            }
            ThatcherModEntityData playerData = (ThatcherModEntityData) player;
            if (player.isHolding(this)) {
                int metLevel = Math.max(EnchantmentHelper.getLevel(ModEnchantments.METALLION.get(), player.getMainHandStack()),
                        EnchantmentHelper.getLevel(ModEnchantments.METALLION.get(), player.getOffHandStack()));
                if (metLevel > 0 && playerData.thatchersRevenge$getPersistentData().getInt("metallion.cooldown") <= 0 &&
                        playerData.thatchersRevenge$getPersistentData().getFloat("metallion.active") <= 0) {
                    playerData.thatchersRevenge$getPersistentData().putFloat("metallion.active", 4 + (metLevel - 1) * 2);
                }
                int enigmaLevel = Math.max(EnchantmentHelper.getLevel(ModEnchantments.ENIGMATIC.get(), player.getMainHandStack()),
                        EnchantmentHelper.getLevel(ModEnchantments.ENIGMATIC.get(), player.getOffHandStack()));
                if (enigmaLevel > 0 && ThreadLocalRandom.current().nextInt(0, 1000 + 1) == 777 && !world.isClient) {
                    player.addStatusEffect(new StatusEffectInstance(
                            BENEFICIAL_EFFS.get(ThreadLocalRandom.current().nextInt(0, BENEFICIAL_EFFS.size() - 1)),
                            140));
                }
                if (player.getOffHandStack().isOf(this)) {
                    List<Entity> nearbyEntities = CommonMethods.getEntitiesNearEntity(player, 6, 3, 6, -6, -3, -6, world);
                    for (Entity mobs : nearbyEntities) {
                        if (mobs instanceof PassiveEntity passive) {
                            lurePassiveEntity(passive, player);
                            ticks++;
                            if (ticks % 10 == 0) {
                                ticks = 0;
                                world.addParticle(ParticleTypes.SOUL, passive.getX(), passive.getBodyY(1.0),
                                        passive.getZ(), 0, 0.1, 0);
                            }
                        }
                    }
                }
            } else {
                playerData.thatchersRevenge$getPersistentData().putFloat("metallion.active", 0.0F);
                playerData.thatchersRevenge$getPersistentData().putInt("etherial.active", 0);
            }
        }
    }

    private void lurePassiveEntity(PassiveEntity passive, PlayerEntity player) {
        passive.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, false, false, false));
        passive.getLookControl().lookAt(player, (float) (passive.getMaxHeadRotation() + 20), (float) passive.getMaxLookPitchChange());
        Path pathToPlayer = passive.getNavigation().findPathTo(player, 6);
        if (passive.distanceTo(player) > 1.5F) {
            if (!(passive instanceof TameableEntity tamable && tamable.isInSittingPose())) {
                passive.getNavigation().startMovingAlong(pathToPlayer, 0.5);
                passive.getMoveControl().moveTo(player.getX(), player.getY(), player.getZ(), 0.5);
            }
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof TameableEntity tameable) {
            if (!tameable.isTamed()) {
                tameable.setTamed(true);
                tameable.setOwner(user);
                stack.damage(3, user, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(3, attacker, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        stack.damage(5, miner, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    public static int getLapisSlotInv(PlayerInventory inventory) {
        Set<Item> items = Set.of(Items.LAPIS_LAZULI);
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.getStack(i);
            if (items.contains(itemStack.getItem()) && itemStack.getCount() > 0) {
                return i;
            }
        }
        return -1337;
    }

    public static boolean getTicksDivisibleBy(ItemStack stack, int divisor) {
        NbtCompound nbtCompound = stack.getNbt();
        return (nbtCompound != null) && (nbtCompound.getInt("ticks") % divisor == 0);
    }

    public static void setTicks(ItemStack stack, int ticks) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt("ticks", ticks);
    }

    static {
        BENEFICIAL_EFFS = List.of(StatusEffects.RESISTANCE, StatusEffects.HASTE, StatusEffects.FIRE_RESISTANCE, StatusEffects.DOLPHINS_GRACE,
                StatusEffects.HERO_OF_THE_VILLAGE, StatusEffects.ABSORPTION, StatusEffects.CONDUIT_POWER, StatusEffects.HEALTH_BOOST,
                StatusEffects.INSTANT_HEALTH, StatusEffects.JUMP_BOOST, StatusEffects.LUCK, StatusEffects.SPEED, StatusEffects.STRENGTH,
                StatusEffects.REGENERATION, StatusEffects.WATER_BREATHING, StatusEffects.NIGHT_VISION, StatusEffects.INVISIBILITY,
                StatusEffects.SATURATION, StatusEffects.SLOW_FALLING);
    }
}
