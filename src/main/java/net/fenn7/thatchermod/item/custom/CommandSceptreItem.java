package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.block.entity.ModEntities;
import net.fenn7.thatchermod.block.entity.custom.CursedMeteorEntity;
import net.fenn7.thatchermod.block.entity.custom.CursedMissileEntity;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ShriekParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSceptreItem extends Item {
    private int ticks;
    public CommandSceptreItem(Settings settings) {
        super(settings);
        this.ticks = 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            user.swingHand(hand, true);
            if (!user.isSneaking()) {
                launchMissileEntity(world, user);
                user.getItemCooldownManager().set(ModItems.COMMAND_SCEPTRE, 1);
                user.getMainHandStack().damage(1, user, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
            else {
                CommonMethods.summonDustParticles(world, 1, 0.0F, 0.0F, 0.33F, 3,
                        user.getX(), user.getY() + 2, user.getZ(), 0, 0, 0);
                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_SKELETON_HORSE_DEATH, SoundCategory.HOSTILE, 8F, 0.75F);
                summonMeteorEntity(world, user);
                user.getItemCooldownManager().set(ModItems.COMMAND_SCEPTRE, 1);
                user.getMainHandStack().damage(3, user, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
        }
        return super.use(world, user, hand);
    }

    private void launchMissileEntity(World world, PlayerEntity user) {
        CursedMissileEntity missileEntity = new CursedMissileEntity(ModEntities.CURSED_MISSILE, world);
        missileEntity.setOwner(user);
        missileEntity.setPos(user.getX(), user.getBodyY(0.7D), user.getZ());
        missileEntity.setVelocity(user, user.getPitch(), user.headYaw, 0, 4.0F, 0.25F);
        world.spawnEntity(missileEntity);
    }

    private void summonMeteorEntity(World world, PlayerEntity user) {
        CursedMeteorEntity meteorEntity = new CursedMeteorEntity(ModEntities.CURSED_METEOR, world);
        meteorEntity.setOwner(user);
        meteorEntity.setFalling(true);

        HitResult hitResult = user.raycast(24, 0, true);
        BlockPos pos;
        switch (hitResult.getType()) {
            case ENTITY -> { pos = ((EntityHitResult) hitResult).getEntity().getBlockPos(); break; }
            case BLOCK -> { pos = ((BlockHitResult) hitResult).getBlockPos(); break; }
            default -> { pos = new BlockPos(hitResult.getPos()); break; }
        }

        BlockPos impactPos = CommonMethods.findFirstNonAirBlockDown(world, pos);
        CommonMethods.summonDustParticles(world, 10, 0, 0, 0.33F, 2,
                impactPos.getX() + 0.5D, impactPos.getY() + 1.5D, impactPos.getZ() + 0.5D,0 ,0 ,0);

        boolean blockFound = false;
        for (int i = 0; i < 16; i++) {
            pos = impactPos.offset(Direction.UP, i + 1);
            if (!world.getBlockState(pos).isAir()) { // stop on the first NON-AIR block
                if (pos.getY()%2 == 1) { // correction needed for odd Y values otherwise meteor will spawn in block
                    pos = pos.offset(Direction.DOWN, 1);
                }
                blockFound = true;
                break;
            }
            i++;
        }
        if (blockFound) { meteorEntity.setPos(pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5); }
        else { meteorEntity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); }
        meteorEntity.setVelocity(0, -64, 0, 0F, 0.0F);
        world.spawnEntity(meteorEntity);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && player.getOffHandStack().getItem() == this) {
            List<Entity> nearbyEntities = CommonMethods.getEntitiesNearPlayer(player, 6, 3, 6, -6, -3, -6, world);
            for (Entity mobs: nearbyEntities) {
                if (mobs instanceof PassiveEntity passive) {
                    lurePassiveEntity(passive, player); ticks++;
                    if (ticks%10 == 0) { ticks = 0;
                        world.addParticle(new ShriekParticleEffect(0), passive.getX(), passive.getY()+passive.getHeight(),
                                passive.getZ(), 0, 1, 0);
                    }
                }
            }
        }
    }

    private void lurePassiveEntity(PassiveEntity passive, PlayerEntity player) {
        passive.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, false, false, false));
        passive.getLookControl().lookAt(player, (float)(passive.getMaxHeadRotation() + 20), (float)passive.getMaxLookPitchChange());
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
                tameable.setTamed(true); tameable.setOwner(user);
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

    public static boolean getTicksDivisibleBy(ItemStack stack, int divisor) {
        NbtCompound nbtCompound = stack.getNbt();
        return (nbtCompound != null) && (nbtCompound.getInt("ticks")%divisor == 0);
    }

    public static void setTicks(ItemStack stack, int ticks) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt("ticks", ticks);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Ability Ready!"));
        } else {
            tooltip.add(Text.literal("Use to cast explosive fireballs"));
            tooltip.add(Text.literal("Use + Sneak to call down a meteor"));
            tooltip.add(Text.literal("Lures passive mobs in offhand"));
        }
    }
}
