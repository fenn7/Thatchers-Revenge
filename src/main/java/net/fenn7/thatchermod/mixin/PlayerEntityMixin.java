package net.fenn7.thatchermod.mixin;

import com.mojang.datafixers.types.templates.Tag;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.LastStandEffect;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.item.custom.ThatcheriteArmourItem;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Thatcherite Armour effects are handled here.
    // HELMET: +50% experience gained, +1 amplifier level and +25% duration to any beneficial effects applied.
    // CHEST: -50% damage from explosions, gain a health boost and speed when damaged by one.
    // LEGS: Apply a slowness effect to the attacker (level increases the higher the damage taken).
    // BOOTS: Reduce damage by 20%, and reflect 33% of it, if moving in the opposite direction as the attacker.

    @Inject(method = "damage", at = @At("HEAD"))
    public void injectDamageMethod(DamageSource source, float amount, CallbackInfoReturnable cir) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (source.isExplosive()) {
            if (ThatcheriteArmourItem.hasChest(player)) {
                player.setStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60, 1), player);
                player.setStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60), player);
                amount = (amount / 2);
            }
            int aaLevel = EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT, player.getEquippedStack(EquipmentSlot.CHEST));
            if (aaLevel != 0 && player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
                amount = (amount / (aaLevel + 1));
            }
        }
        if (source.getAttacker() instanceof LivingEntity attacker && attacker != player) {
            if (ThatcheriteArmourItem.hasLegs(player)) {
                attacker.setStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40,
                        (int) (amount/4) - 1), player);
            }
            if (ThatcheriteArmourItem.hasBoots(player)) {
                Direction enemyDirection = attacker.getMovementDirection();
                Direction playerDirection = player.getMovementDirection();
                if (enemyDirection == playerDirection.getOpposite()) {
                    attacker.damage(DamageSource.GENERIC, amount/3);
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
        IEntityDataSaver playerData = (IEntityDataSaver) player;
        List<ItemStack> stackList = new ArrayList<>();
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (EnchantmentHelper.getLevel(ModEnchantments.BAILOUT, stack) != 0) {
                stack.setDamage(stack.getMaxDamage() - 1);
                stackList.add(stack);
                player.getInventory().removeStack(i);
            }
            if (!stackList.isEmpty()) {
                NbtList nbtList = new NbtList();
                Iterator stackIterator = stackList.iterator();

                while(stackIterator.hasNext()) {
                    ItemStack bailoutStack = (ItemStack) stackIterator.next();
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
    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (player.hasStatusEffect(ModEffects.LAST_STAND)) {
            LastStandEffect.setStatusOnRemove(player, true);
            player.removeStatusEffect(ModEffects.LAST_STAND);

            float newHealth = other.getMaxHealth()/2;
            if (newHealth > 10) { newHealth = 10; }
            player.setHealth(newHealth);

            LastStandEffect.setStatusOnRemove(player, false);
            player.playSound(SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.HOSTILE, 100F, 2.0F);
        }
        return super.onKilledOther(world, other);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void injectTickAirAssaultMethod(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        int aaLevel = (EnchantmentHelper.getLevel(ModEnchantments.AIR_ASSAULT, chest));
        if (player.isFallFlying() && chest.isOf(Items.ELYTRA) && ElytraItem.isUsable(chest) && aaLevel != 0) {
            if (!world.isClient && (player.getRoll() + 1)%(25 - 2 * aaLevel) == 0) {
                BlockPos pos = player.getBlockPos();
                while (world.getBlockState(pos).isAir() && pos.getY() >= -64) {
                    pos = pos.offset(Direction.DOWN, 1);
                    if (!world.getBlockState(pos).isAir()) {
                        break;
                    }
                }
                world.createExplosion(player, player.getX(), pos.getY() + 1, player.getZ(), (float) aaLevel/2,
                        Explosion.DestructionType.NONE);
            }
        }
    }
}


