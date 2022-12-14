package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.effect.LastStandEffect;
import net.fenn7.thatchermod.commonside.effect.ModEffects;
import net.fenn7.thatchermod.commonside.enchantments.ModEnchantments;
import net.fenn7.thatchermod.commonside.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.commonside.entity.projectiles.GrenadeEntity;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.ThatcheriteArmourItem;
import net.fenn7.thatchermod.commonside.util.ThatcherModEntityData;
import net.fenn7.thatchermod.commonside.util.NBTInterface;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    private int recoilTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Thatcherite Armour effects are handled here.
    // HELMET: +50% experience gained, +1 amplifier level and +25% duration to any beneficial effects applied.
    // CHEST: -50% damage from explosions, gain a health boost and speed when damaged by one.
    // LEGS: Apply a slowness effect to the attacker (level increases the higher the damage taken).
    // BOOTS: Reduce damage by 20%, and reflect 25% of it, if player can see the attacker.

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void injectDamageMethod(DamageSource source, float amount, CallbackInfoReturnable cir) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        float preMitigationDmg = amount;
        if (player.hasStatusEffect(ModEffects.LAST_STAND.get())) {
            amount -= amount;
            cir.cancel();
        }
        if (source.isExplosive()) {
            if (ThatcheriteArmourItem.hasChest(player)) {
                player.setStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60, 1), player);
                player.setStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60), player);
                amount /= 2;
            }
            int aaLevel = EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT.get(), player.getEquippedStack(EquipmentSlot.CHEST));
            if (aaLevel != 0 && player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
                amount /= aaLevel;
            }
        }
        if (source.getAttacker() instanceof LivingEntity attacker && attacker != player) {
            if (ThatcheriteArmourItem.hasLegs(player)) {
                attacker.setStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40,
                        (int) (preMitigationDmg / 4) - 1), player);
            }
            if (ThatcheriteArmourItem.hasBoots(player)) {
                if (player.canSee(attacker)) {
                    attacker.damage(DamageSource.GENERIC, preMitigationDmg / 4);
                    amount -= (amount / 5);
                }
            }
        }
    }

    @Inject(method = "addExperience", at = @At("HEAD"))
    public void injectExperienceMethod(int experience, CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (ThatcheriteArmourItem.hasHelmet(player)) {
            experience = (int) (1.5 * experience);
        }
    }

    @Inject(method = "dropInventory", at = @At("HEAD"))
    public void injectDropInventoryMethod(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (!player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            ThatcherModEntityData playerData = (ThatcherModEntityData) player;
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (EnchantmentHelper.getLevel(ModEnchantments.BAILOUT.get(), stack) != 0) {
                    stack.setDamage((int) Math.ceil(stack.getDamage() / 2.0F));
                    stackList.add(stack);
                    player.getInventory().removeStack(i);
                }
            }
            if (!stackList.isEmpty()) {
                NbtList nbtList = new NbtList();
                for (ItemStack bailoutStack : stackList) {
                    nbtList.add(bailoutStack.writeNbt(new NbtCompound()));
                }
                playerData.getPersistentData().put("bailout.items", nbtList);
            }
        }
    }

    @Override
    public boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (ThatcheriteArmourItem.hasHelmet(player) && effect.getEffectType().isBeneficial()) {
            int newLevel = effect.getAmplifier() + 1;
            effect = new StatusEffectInstance(effect.getEffectType(), (int) (1.25 * effect.getDuration()), newLevel,
                    effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon());
        }
        return super.addStatusEffect(effect, source);
    }

    @Override
    public void onKilledOther(ServerWorld world, LivingEntity other) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (player.hasStatusEffect(ModEffects.LAST_STAND.get())) {
            LastStandEffect.setStatusOnRemove(player, true);
            player.removeStatusEffect(ModEffects.LAST_STAND.get());

            float newHealth = other.getMaxHealth() / 2;
            if (newHealth > 10) {
                newHealth = 10;
            }
            player.setHealth(newHealth);

            LastStandEffect.setStatusOnRemove(player, false);
            player.playSound(SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.HOSTILE, 2.0F, 3.0F);
        }
        super.onKilledOther(world, other);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void injectTickingMethod(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        ThatcherModEntityData playerData = (ThatcherModEntityData) player;
        int lastStandCDTicks = playerData.getPersistentData().getInt("last.stand.cooldown");
        if (lastStandCDTicks > 0) {
            playerData.getPersistentData().putInt("last.stand.cooldown", --lastStandCDTicks);
            ThatcherMod.LOGGER.warn("" + playerData.getPersistentData().getInt("last.stand.cooldown"));
        }
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST); // Elytra Enchantments
        if (chest.isOf(Items.ELYTRA) && !world.isClient) {
            int interceptLevel = EnchantmentHelper.getLevel(ModEnchantments.INTERCEPTOR.get(), chest);
            if (interceptLevel != 0 && player.age % (40 - ((interceptLevel - 1) * 10)) == 0) {
                int arrowSlot = findIndexOfItems(player.getInventory(), Set.of(Items.ARROW));
                if (player.isCreative() || arrowSlot != -69) {
                    int range = 8 + (interceptLevel - 1) * 3;
                    Box interceptBox = new Box(player.getBlockPos()).expand(range);
                    List<Entity> nearEntitiesList = world.getOtherEntities(player, interceptBox).stream()
                            .filter(e -> e instanceof MobEntity alive &&
                                    alive.getTarget() != null &&
                                    alive.getTarget() == player &&
                                    alive.distanceTo(player) <= range).toList();
                    int maximumShots = nearEntitiesList.size();
                    if (maximumShots > 3) maximumShots = 3;
                    for (int i = 0; i < maximumShots; ++i) {
                        if (nearEntitiesList.isEmpty() || !player.isCreative() && player.getInventory().getStack(arrowSlot).isEmpty()) {
                            break;
                        }
                        int index = ThreadLocalRandom.current().nextInt(0, nearEntitiesList.size());
                        Entity attacker = nearEntitiesList.get(index);
                        PersistentProjectileEntity arrow = ProjectileUtil.createArrowProjectile(player, new ItemStack(Items.ARROW), 1.0F);
                        arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                        arrow.setDamage(arrow.getDamage() + (double) interceptLevel * 0.5D + 0.5);
                        double velX = attacker.getX() - player.getX();
                        double velY = attacker.getBodyY(0.5D) - arrow.getY();
                        double velZ = attacker.getZ() - player.getZ();
                        double g = Math.sqrt(Math.pow(velX, 2) + Math.pow(velZ, 2));
                        arrow.setVelocity(velX, velY + g * 0.2D, velZ, 1.6F, 2.0F);
                        world.spawnEntity(arrow);
                        if (!player.isCreative()) {
                            player.getInventory().getStack(arrowSlot).decrement(1);
                            chest.damage(1, player, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                        }
                    }
                }
            }
            if (player.isFallFlying() && ElytraItem.isUsable(chest)) {
                int aaLevel = EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT.get(), chest);
                int stLevel = EnchantmentHelper.getLevel(ModEnchantments.STEALTH.get(), chest);
                boolean isBombing = NBTInterface.isBombing(chest);
                if (aaLevel != 0 && isBombing && player.getRoll() + 1 >= 60 && (player.getRoll() + 1) % (25 - 2 * aaLevel) == 0) {
                    int grenadeSlot = findIndexOfItems(player.getInventory(), Set.of(ModItems.GRENADE.get()));
                    if (player.isCreative() || grenadeSlot != -69) {
                        AbstractGrenadeEntity grenade = new GrenadeEntity(world, player);
                        grenade.setShouldBounce(false);
                        grenade.setMaxAgeTicks(12000);
                        grenade.setPower(aaLevel * 0.5F + 0.5F);
                        grenade.setPos(player.getX(), player.getY() - 1, player.getZ());
                        world.spawnEntity(grenade);
                        if (!player.isCreative()) {
                            player.getInventory().getStack(grenadeSlot).decrement(1);
                            chest.damage(2, player, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                        }
                    }
                }
                if (stLevel != 0) {
                    player.setStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 2, 0, false, false), player);
                }
            }
        }
        int prLevel = EnchantmentHelper.getLevel(ModEnchantments.PRIVATISATION.get(), player.getMainHandStack());
        if (prLevel != 0) {
            Vec3d pos = player.getPos();
            BlockPos pos1 = new BlockPos(pos.getX() - prLevel - 1, pos.getY() - prLevel - 1, pos.getZ() - prLevel - 1);
            BlockPos pos2 = new BlockPos(pos.getX() + prLevel + 1, pos.getY() + prLevel + 1, pos.getZ() + prLevel + 1);
            Box box = new Box(pos1, pos2);
            List<Entity> entityList = world.getOtherEntities(null, box);
            entityList.stream().filter(e -> e instanceof ItemEntity)
                    .forEach(e -> player.giveItemStack(((ItemEntity) e).getStack()));
        }
    }

    private int findIndexOfItems(PlayerInventory inventory, Set<Item> items) {
        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.getStack(i);
            if (items.contains(itemStack.getItem()) && itemStack.getCount() > 0) {
                return i;
            }
        }
        return -69;
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void injectTickHeadTiltMethod(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        ThatcherModEntityData playerData = (ThatcherModEntityData) player;

        if (playerData.getPersistentData().getBoolean("should_recoil") && player.world.isClient()) {
            if (this.recoilTicks == 0) {
                Vec3d vel = Vec3d.fromPolar(player.getPitch(), player.getYaw());
                player.takeKnockback(0.2D, vel.getX(), vel.getZ());
            }

            this.recoilTicks++;
            if (this.recoilTicks <= 5) {
                player.setPitch(player.getPitch() - 3.2F);
            } else {
                playerData.getPersistentData().putBoolean("should_recoil", false);
                this.recoilTicks = 0;
            }
        }
    }
}


