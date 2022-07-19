package net.fenn7.thatchermod.effect;

import net.fenn7.thatchermod.block.entity.ModEntities;
import net.fenn7.thatchermod.particle.ModParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class CalciumDeficiencyEffect extends StatusEffect {
    protected CalciumDeficiencyEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if ((entity.getType() != ModEntities.THATCHER) && !entity.world.isClient) {
            // every level of this reduces speed by 20%, damage and armour by 15% up to 80% & 60% maximum at level 4.
            // (this is implemented in ModEffects).
            if (amplifier > 3) {
                for (EntityAttributeModifier modifier : this.getAttributeModifiers().values()) {
                    adjustModifierAmount(3, modifier);
                }
            }

            ((ServerWorld)entity.world).spawnParticles(ModParticles.DEFICIENCY_INDICATOR.getType(),
                    entity.getX(), entity.getY() + entity.getHeight() + 0.5D, entity.getZ(), 1,
                    0, 0, 0, 0);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
