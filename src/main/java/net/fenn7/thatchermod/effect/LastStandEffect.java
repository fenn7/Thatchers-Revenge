package net.fenn7.thatchermod.effect;

import net.fenn7.thatchermod.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.ThreadLocalRandom;

public class LastStandEffect extends StatusEffect {
    private int ticks;
    private int maxTicks;
    private String fightMsg;
    private final ServerBossBar bossBar;

    protected LastStandEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        this.ticks = 0;
        this.maxTicks = 120;
        this.fightMsg = "";
        this.bossBar = (ServerBossBar) (new ServerBossBar(Text.literal("§0LAST STAND ACTIVE"), BossBar.Color.RED, BossBar.Style.PROGRESS)).setDarkenSky(true);
    }

    public LastStandEffect(StatusEffectCategory statusEffectCategory, int color, int maxTicks) {
        this(statusEffectCategory, color);
        this.maxTicks = maxTicks;
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
                this.bossBar.setPercent((float) this.ticks / this.maxTicks);
                this.bossBar.addPlayer((ServerPlayerEntity) player);
            }
            player.addExhaustion(-1);
            if (this.ticks % 40 == 0) {
                player.world.playSound(null, player.getBlockPos(), new SoundEvent(new Identifier("thatchermod:last_heartbeat")),
                        SoundCategory.HOSTILE, 80, 1.25F);
                player.sendMessage(Text.literal(this.fightMsg), true);
            }
            if (!player.world.isClient) {
                this.ticks++;
            }
            if (this.ticks >= this.maxTicks) {
                this.ticks = 0;
            }
        }
    }

    public String generateMessage() {
        String message = "§4";
        int random = ThreadLocalRandom.current().nextInt(0, 5 + 1);
        switch (random) {
            case 1:
                message += "FIGHT OR BE FORGOTTEN";
                break;
            case 2:
                message += "IT'S KILL OR BE KILLED";
                break;
            case 3:
                message += "DROWN THEM IN A SEA OF BLOOD";
                break;
            case 4:
                message += "DEATH IS ONLY THE END FOR THEM";
                break;
            case 5:
                message += "RENEWAL IS PAID FOR IN FLESH";
                break;
        }
        return message;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.setHealth(1F);
        setMaxTicks(entity.getStatusEffect(ModEffects.LAST_STAND).getDuration());
        this.fightMsg = generateMessage();

        if (entity instanceof PlayerEntity player) {
            IEntityDataSaver entityData = ((IEntityDataSaver) player);
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
                player.world.playSound(null, player.getBlockPos(),
                        new SoundEvent(new Identifier("thatchermod:heart_overload")), SoundCategory.HOSTILE, 100, 1);
            }
            this.bossBar.removePlayer((ServerPlayerEntity) player);
        } else if (!shouldLiveOnRemove(entity)) {
            entity.kill();
        }

        this.ticks = 0;
        entity.setGlowing(false);
        entity.setInvulnerable(false);
        entity.setCustomNameVisible(false);

        super.onRemoved(entity, attributes, amplifier);
    }

    public static boolean shouldLiveOnRemove(Entity entity) {
        IEntityDataSaver entityData = ((IEntityDataSaver) entity);
        return entityData.getPersistentData().getBoolean("should_live_post_stand");
    }

    public static void setStatusOnRemove(Entity entity, boolean shouldLive) {
        IEntityDataSaver entityData = ((IEntityDataSaver) entity);
        entityData.getPersistentData().putBoolean("should_live_post_stand", shouldLive);
    }
}
