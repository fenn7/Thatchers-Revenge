package net.fenn7.thatchermod.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fenn7.thatchermod.effect.LastStandEffect;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.ThatcheriteArmourItem;
import net.fenn7.thatchermod.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.sound.ModSounds;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class PlayerEvents implements ServerPlayerEvents.AfterRespawn, ServerPlayerEvents.AllowDeath {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        int unionBusterCD = (int) (UnionBusterItem.DURATION *
                oldPlayer.getItemCooldownManager().getCooldownProgress(ModItems.UNION_BUSTER, 0));
        newPlayer.getItemCooldownManager().set(ModItems.UNION_BUSTER, unionBusterCD);
    }

    @Override
    public boolean allowDeath(ServerPlayerEntity player, DamageSource damageSource, float damageAmount) {
        if (ThatcheriteArmourItem.hasFullSet(player) && !damageSource.isOutOfWorld()) {
            player.playSound(SoundEvents.ENTITY_WITHER_HURT, SoundCategory.HOSTILE, 20F, 0.5F);
            player.setStatusEffect(new StatusEffectInstance(ModEffects.LAST_STAND, 120, 0), null);
            return false;
        }
        else {
            return true;
        }
    }
}
