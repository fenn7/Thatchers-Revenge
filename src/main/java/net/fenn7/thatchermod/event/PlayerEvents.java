package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.LastStandEffect;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.ThatcheriteArmourItem;
import net.fenn7.thatchermod.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.sound.ModSounds;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class PlayerEvents implements ServerPlayerEvents.AfterRespawn, ServerPlayerEvents.AllowDeath, ServerPlayerEvents.CopyFrom {

    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        int unionBusterCD = (int) (UnionBusterItem.DURATION *
                oldPlayer.getItemCooldownManager().getCooldownProgress(ModItems.UNION_BUSTER, 0));
        newPlayer.getItemCooldownManager().set(ModItems.UNION_BUSTER, unionBusterCD);

        IEntityDataSaver playerData = ((IEntityDataSaver) oldPlayer);
        if (playerData.getPersistentData().contains("bailout.items", 9)) {
            NbtList nbtList = playerData.getPersistentData().getList("bailout.items", 10);
            for (int i = 0; i < nbtList.size(); i++) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                ItemStack stack = ItemStack.fromNbt(nbtCompound);
                newPlayer.world.spawnEntity(new ItemEntity(newPlayer.world, newPlayer.getX(), newPlayer.getY(), newPlayer.getZ(), stack));
            }
            nbtList.clear();
            playerData.getPersistentData().put("bailout.items", nbtList);
        }
    }

    @Override
    public boolean allowDeath(ServerPlayerEntity player, DamageSource damageSource, float damageAmount) {
        if (ThatcheriteArmourItem.hasFullSet(player) && !damageSource.isOutOfWorld()
            && player.getMainHandStack().getItem() != Items.TOTEM_OF_UNDYING
            && player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            player.playSound(SoundEvents.ENTITY_WITHER_HURT, SoundCategory.HOSTILE, 20F, 0.5F);
            player.setStatusEffect(new StatusEffectInstance(ModEffects.LAST_STAND, 120, 0), null);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
    }
}
