package net.fenn7.thatchermod.commonside.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.fenn7.thatchermod.commonside.effect.ModEffects;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.item.custom.ThatcheriteArmourItem;
import net.fenn7.thatchermod.commonside.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.commonside.util.IEntityDataSaver;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public enum PlayerEvents implements EntityEvent.LivingDeath, PlayerEvent.PlayerClone {

    INSTANCE;

    @Override
    public void clone(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean wonGame) {
        int unionBusterCD = (int) (UnionBusterItem.DURATION *
                oldPlayer.getItemCooldownManager().getCooldownProgress(ModItems.UNION_BUSTER.get(), 0));
        newPlayer.getItemCooldownManager().set(ModItems.UNION_BUSTER.get(), unionBusterCD);

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
    public EventResult die(LivingEntity entity, DamageSource source) {
        if (entity instanceof PlayerEntity player
                && ThatcheriteArmourItem.hasFullSet(player) && !source.isOutOfWorld()
                && player.getMainHandStack().getItem() != Items.TOTEM_OF_UNDYING
                && player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            player.playSound(SoundEvents.ENTITY_WITHER_HURT, SoundCategory.HOSTILE, 20F, 0.5F);
            player.setStatusEffect(new StatusEffectInstance(ModEffects.LAST_STAND.get(), 120, 0), null);
            return EventResult.interruptFalse();
        } else {
            return EventResult.pass();
        }
    }
}