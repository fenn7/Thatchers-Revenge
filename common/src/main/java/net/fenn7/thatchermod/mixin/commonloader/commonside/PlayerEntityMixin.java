package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.fenn7.thatchermod.commonside.effect.LastStandEffect;
import net.fenn7.thatchermod.commonside.effect.ModEffects;
import net.fenn7.thatchermod.commonside.enchantments.ModEnchantments;
import net.fenn7.thatchermod.commonside.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.commonside.entity.projectiles.GrenadeEntity;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.ThatcheriteArmourItem;
import net.fenn7.thatchermod.commonside.util.NBTInterface;
import net.fenn7.thatchermod.commonside.util.ThatcherModEntityData;
import net.fenn7.thatchermod.mixin.commonloader.commonside.accessor.StatusEffectInstanceAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements ThatcherModEntityData {

    @Shadow
    public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract PlayerInventory getInventory();

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean giveItemStack(ItemStack stack);

    private int thatchersRevenge$recoilTicks = 0;

    // Thatcherite Armour effects are handled here.
    // HELMET: +50% experience gained, +1 amplifier level and +25% duration to any beneficial effects applied.
    // CHEST: -50% damage from explosions, gain a health boost and speed when damaged by one.
    // LEGS: Apply a slowness effect to the attacker (level increases the higher the damage taken).
    // BOOTS: Reduce damage by 20%, and reflect 25% of it, if player can see the attacker.

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void thatchersRevenge$injectDamageMethod(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        float preMitigationDmg = amount;
        if (hasStatusEffect(ModEffects.LAST_STAND.get())) {
            amount -= amount;
            cir.cancel();
        }
        PlayerEntity thisPlayer = (PlayerEntity) (Object) this;
        if (source.isExplosive()) {
            if (ThatcheriteArmourItem.hasChest(thisPlayer)) {
                setStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60, 1), thisPlayer);
                setStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60), thisPlayer);
                amount /= 2;
            }
            int aaLevel = EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT.get(), thisPlayer.getEquippedStack(EquipmentSlot.CHEST));
            if (aaLevel != 0 && getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
                amount /= aaLevel;
            }
        }
        if (source.getAttacker() instanceof LivingEntity attacker && !equals(attacker)) {
            if (ThatcheriteArmourItem.hasLegs(thisPlayer)) {
                attacker.setStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, (int) (preMitigationDmg / 4) - 1), thisPlayer);
            }
            if (ThatcheriteArmourItem.hasBoots(thisPlayer)) {
                if (canSee(attacker)) {
                    attacker.damage(DamageSource.GENERIC, preMitigationDmg / 4);
                    amount -= (amount / 5);
                }
            }
        }
    }

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int thatchersRevenge$multiplyExperience(int experience) {
        PlayerEntity thisPlayer = ((PlayerEntity) (Object) this);
        if (ThatcheriteArmourItem.hasHelmet(thisPlayer)) {
            experience = (int) (1.5 * experience);
        }
        return experience;
    }

    @Inject(method = "dropInventory", at = @At("HEAD"))
    private void thatchersRevenge$injectDropInventoryMethod(CallbackInfo ci) {
        if (!world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < getInventory().size(); i++) {
                ItemStack stack = getInventory().getStack(i);
                if (EnchantmentHelper.getLevel(ModEnchantments.BAILOUT.get(), stack) != 0) {
                    stack.setDamage((int) Math.ceil(stack.getDamage() / 2.0F));
                    stackList.add(stack);
                    getInventory().removeStack(i);
                }
            }
            if (!stackList.isEmpty()) {
                NbtList nbtList = new NbtList();
                for (ItemStack bailoutStack : stackList) {
                    nbtList.add(bailoutStack.writeNbt(new NbtCompound()));
                }
                thatchersRevenge$getPersistentData().put("bailout.items", nbtList);
            }
        }
    }

    @Override
    protected void thatchersRevenge$setLastStandEffect(ServerWorld world, LivingEntity other, CallbackInfo ci) {
        if (hasStatusEffect(ModEffects.LAST_STAND.get())) {
            PlayerEntity thisPlayer = ((PlayerEntity) (Object) this);
            LastStandEffect.setStatusOnRemove(thisPlayer, true);
            removeStatusEffect(ModEffects.LAST_STAND.get());

            float newHealth = other.getMaxHealth() / 2;
            if (newHealth > 10) {
                newHealth = 10;
            }
            setHealth(newHealth);

            LastStandEffect.setStatusOnRemove(thisPlayer, false);
            playSound(SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.HOSTILE, 2.0F, 3.0F);
        }
    }

    @Override
    protected void thatchersRevenge$buffStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity thisPlayer = ((PlayerEntity) (Object) this);
        if (ThatcheriteArmourItem.hasHelmet(thisPlayer) && effect.getEffectType().isBeneficial()) {
            StatusEffectInstanceAccessor accessor = (StatusEffectInstanceAccessor) effect;
            accessor.thatchersRevenge$setDuration((int) (1.25 * effect.getDuration()));
            accessor.thatchersRevenge$setAmplifier(effect.getAmplifier() + 1);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void thatchersRevenge$injectTickingMethod(CallbackInfo ci) {
        PlayerEntity thisPlayer = ((PlayerEntity) (Object) this);
        int lastStandCDTicks = thatchersRevenge$getPersistentData().getInt("last.stand.cooldown");
        if (lastStandCDTicks > 0) {
            thatchersRevenge$getPersistentData().putInt("last.stand.cooldown", --lastStandCDTicks);
        } else if (ThatcheriteArmourItem.hasFullSet(thisPlayer) && lastStandCDTicks <= 0) {
            world.addParticle(ParticleTypes.PORTAL, getX(), getY(), getZ(), 0, 0.5D, 0);
        }
        ItemStack chest = getEquippedStack(EquipmentSlot.CHEST); // Elytra Enchantments
        if (chest.isOf(Items.ELYTRA) && !world.isClient) {
            int interceptLevel = EnchantmentHelper.getLevel(ModEnchantments.INTERCEPTOR.get(), chest);
            if (interceptLevel != 0 && age % (40 - ((interceptLevel - 1) * 10)) == 0) {
                int arrowSlot = thatchersRevenge$findIndexOfItems(getInventory(), Set.of(Items.ARROW));
                if (isCreative() || arrowSlot != -69) {
                    int range = 8 + (interceptLevel - 1) * 3;
                    Box interceptBox = new Box(getBlockPos()).expand(range);
                    List<Entity> nearEntitiesList = world.getOtherEntities(thisPlayer, interceptBox).stream()
                            .filter(e -> e instanceof MobEntity alive &&
                                    alive.getTarget() == thisPlayer &&
                                    alive.distanceTo(thisPlayer) <= range).toList();
                    int maximumShots = nearEntitiesList.size();
                    if (maximumShots > 3) maximumShots = 3;
                    for (int i = 0; i < maximumShots; ++i) {
                        if (nearEntitiesList.isEmpty() || !thisPlayer.isCreative() && thisPlayer.getInventory().getStack(arrowSlot).isEmpty()) {
                            break;
                        }
                        int index = ThreadLocalRandom.current().nextInt(0, nearEntitiesList.size());
                        Entity attacker = nearEntitiesList.get(index);
                        PersistentProjectileEntity arrow = ProjectileUtil.createArrowProjectile(thisPlayer, new ItemStack(Items.ARROW), 1.0F);
                        arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                        arrow.setDamage(arrow.getDamage() + (double) interceptLevel * 0.5D + 0.5);
                        double velX = attacker.getX() - getX();
                        double velY = attacker.getBodyY(0.5D) - arrow.getY();
                        double velZ = attacker.getZ() - getZ();
                        double g = Math.sqrt(Math.pow(velX, 2) + Math.pow(velZ, 2));
                        arrow.setVelocity(velX, velY + g * 0.2D, velZ, 1.6F, 2.0F);
                        world.spawnEntity(arrow);
                        if (!isCreative()) {
                            getInventory().getStack(arrowSlot).decrement(1);
                            chest.damage(1, thisPlayer, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                        }
                    }
                }
            }
            if (isFallFlying() && ElytraItem.isUsable(chest)) {
                int aaLevel = EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT.get(), chest);
                int stLevel = EnchantmentHelper.getLevel(ModEnchantments.STEALTH.get(), chest);
                boolean isBombing = NBTInterface.isBombing(chest);
                if (aaLevel != 0 && isBombing && getRoll() + 1 >= 60 && (getRoll() + 1) % (25 - 2 * aaLevel) == 0) {
                    int grenadeSlot = thatchersRevenge$findIndexOfItems(thisPlayer.getInventory(), Set.of(ModItems.GRENADE.get()));
                    if (isCreative() || grenadeSlot != -69) {
                        AbstractGrenadeEntity grenade = new GrenadeEntity(world, thisPlayer);
                        grenade.setShouldBounce(false);
                        grenade.setMaxAgeTicks(12000);
                        grenade.setPower(aaLevel * 0.5F + 0.5F);
                        grenade.setPos(getX(), getY() - 1, getZ());
                        world.spawnEntity(grenade);
                        if (!isCreative()) {
                            getInventory().getStack(grenadeSlot).decrement(1);
                            chest.damage(2, thisPlayer, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                        }
                    }
                }
                if (stLevel != 0) {
                    setStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 2, 0, false, false), thisPlayer);
                }
            }
        }
        int prLevel = EnchantmentHelper.getLevel(ModEnchantments.PRIVATISATION.get(), getMainHandStack());
        if (prLevel != 0) {
            Vec3d pos = getPos();
            BlockPos pos1 = new BlockPos(pos.getX() - prLevel - 1, pos.getY() - prLevel - 1, pos.getZ() - prLevel - 1);
            BlockPos pos2 = new BlockPos(pos.getX() + prLevel + 1, pos.getY() + prLevel + 1, pos.getZ() + prLevel + 1);
            Box box = new Box(pos1, pos2);
            List<Entity> entityList = world.getOtherEntities(null, box);
            entityList.stream().filter(e -> e instanceof ItemEntity)
                    .forEach(e -> giveItemStack(((ItemEntity) e).getStack()));
        }
    }

    private int thatchersRevenge$findIndexOfItems(PlayerInventory inventory, Set<Item> items) {
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.getStack(i);
            if (items.contains(itemStack.getItem()) && itemStack.getCount() > 0) {
                return i;
            }
        }
        return -69;
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void thatchersRevenge$injectTickHeadTiltMethod(CallbackInfo ci) {
        if (thatchersRevenge$getPersistentData().getBoolean("should_recoil") && world.isClient()) {
            if (thatchersRevenge$recoilTicks == 0) {
                Vec3d vel = Vec3d.fromPolar(getPitch(), getYaw());
                takeKnockback(0.2D, vel.getX(), vel.getZ());
            }
            thatchersRevenge$recoilTicks++;
            if (thatchersRevenge$recoilTicks <= 5) {
                setPitch(getPitch() - 3.2F);
            } else {
                thatchersRevenge$getPersistentData().putBoolean("should_recoil", false);
                thatchersRevenge$recoilTicks = 0;
            }
        }
    }
}
