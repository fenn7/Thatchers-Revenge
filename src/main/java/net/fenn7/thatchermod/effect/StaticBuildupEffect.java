package net.fenn7.thatchermod.effect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class StaticBuildupEffect extends StatusEffect {
    private int ticks;
    private boolean shouldRemoveSafely = true;

    protected StaticBuildupEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        this.ticks = 0;
    }

    public StaticBuildupEffect() {
        this(StatusEffectCategory.NEUTRAL, 0);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.world.isClient) {
            for (int i = 0; i <= (amplifier); i++) {
                ((ServerWorld) entity.world).spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                        entity.getX(), entity.getY() + 1 + entity.getHeight() + (float) ((i+1)/2), entity.getZ(), 1,
                        0, 0, 0, 0);
                if (ticks%20 == 0) {
                    ((ServerWorld) entity.world).spawnParticles(ParticleTypes.END_ROD,
                            entity.getX(), entity.getY() + 1 + entity.getHeight() + (float) ((i + 1) / 2), entity.getZ(), 1,
                            0, 0, 0, 0);
                    ((ServerWorld) entity.world).spawnParticles(ParticleTypes.ENCHANTED_HIT,
                            entity.getX(), entity.getY() + 1 + entity.getHeight() + (float) ((i + 1) / 2), entity.getZ(), 1,
                            0, 0, 0, 0);
                }
            }
        }
        this.ticks++; if (this.ticks >= 19) { this.ticks = 0; }
        super.applyUpdateEffect(entity, amplifier);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.world.isClient && !this.shouldRemoveSafely) {
            for (int i = 0; i <= (amplifier); i++) {
                entity.world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 8F, 0.75F);
                LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, entity.world);
                lightning.setPos(entity.getX(), entity.getY(), entity.getZ());
                entity.world.spawnEntity(lightning);
            }
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    public void setShouldRemoveSafely(boolean safe) {
        this.shouldRemoveSafely = safe;
    }
}

