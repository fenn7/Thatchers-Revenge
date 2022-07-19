package net.fenn7.thatchermod.effect;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.concurrent.ThreadLocalRandom;

public class LastStandEffect extends StatusEffect {
    private int ticks;
    private int maxTicks;
    private String fightMsg;
    private ServerBossBar bossBar;

    protected LastStandEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        this.ticks = 0;
        this.maxTicks = 100;
        this.fightMsg = "";
        this.bossBar = (ServerBossBar)(new ServerBossBar(Text.literal("§0LAST STAND ACTIVE"), BossBar.Color.RED, BossBar.Style.PROGRESS)).setDarkenSky(true);
    }

    public LastStandEffect(StatusEffectCategory statusEffectCategory, int color, int maxTicks) {
        super(statusEffectCategory, color);
        this.ticks = 0;
        this.maxTicks = maxTicks;
        this.fightMsg = "";
        this.bossBar = (ServerBossBar)(new ServerBossBar(Text.literal("§0LAST STAND ACTIVE"), BossBar.Color.RED, BossBar.Style.PROGRESS)).setDarkenSky(true);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // reduces max health by 95% but makes the entity invulnerable to damage.
        // also induces a "berserker state": increases attack speed and damage by 35%
        // kills the entity after, unless it is a player who kills another entity (handled elsewhere)
        // max level 1

        if (amplifier > 0) {
            for (EntityAttributeModifier modifier : this.getAttributeModifiers().values()) {
                adjustModifierAmount(0, modifier);
            }
        }
        entity.setCustomName(Text.literal("§4LAST STAND ACTIVE"));
        entity.setGlowing(true);
        entity.setInvulnerable(true);
        entity.setCustomNameVisible(true);

        if (entity instanceof PlayerEntity player) {
            if (!player.world.isClient) {
                this.bossBar.setPercent((float) this.ticks / this.maxTicks );
                this.bossBar.addPlayer((ServerPlayerEntity) player);
            }
            player.addExhaustion(-1);

            if (this.ticks%20 == 0 || this.ticks%20 == 5) {
                player.world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.PLAYERS,
                        100F, 1.0F);
                player.sendMessage(Text.literal(this.fightMsg), true);
            }
            this.ticks++;
            if (this.ticks > this.maxTicks) {
                this.ticks = 0;
            }
        }
    }

    public String generateMessage() {
        String message = "§4";
        int random = ThreadLocalRandom.current().nextInt(0, 5 + 1);
        switch (random) {
            case 1: message += "FIGHT OR BE FORGOTTEN"; break;
            case 2: message += "IT'S KILL OR BE KILLED"; break;
            case 3: message += "DROWN THEM IN A SEA OF BLOOD"; break;
            case 4: message += "DEATH IS ONLY THE END FOR THEM"; break;
            case 5: message += "RENEWAL IS PAID FOR IN FLESH"; break;
        }
        return message;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.setHealth(1F);
        this.fightMsg = generateMessage();
        //this.maxTicks = entity.getStatusEffect(ModEffects.LAST_STAND).getDuration();
        if (entity instanceof PlayerEntity player) {
            this.bossBar.addPlayer((ServerPlayerEntity) player);
        }
        super.onApplied(entity, attributes, amplifier);
    }

    public void setMaxTicks(int max) {
        this.maxTicks = max;
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            if (!player.isSpectator() && !player.isCreative() && !shouldLiveOnRemove(entity)) {
                entity.kill();
            }
            this.bossBar.removePlayer((ServerPlayerEntity) player);
        }
        else if (!shouldLiveOnRemove(entity)) {
            entity.kill();
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    public static boolean shouldLiveOnRemove(Entity entity) {
        IEntityDataSaver entityData = ((IEntityDataSaver) entity);
        return entityData.getPersistentData().getBoolean("should_live_post_stand");
    }

    public static void setStatusOnRemove(Entity entity, boolean death) {
        IEntityDataSaver entityData = ((IEntityDataSaver) entity);
        entityData.getPersistentData().putBoolean("should_live_post_stand", death);
    }
}
