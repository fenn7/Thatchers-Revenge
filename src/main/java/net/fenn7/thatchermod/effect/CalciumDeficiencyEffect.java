package net.fenn7.thatchermod.effect;

import net.fenn7.thatchermod.block.entity.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

public class CalciumDeficiencyEffect extends StatusEffect {
    protected CalciumDeficiencyEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if ((entity.getType() != ModEntities.THATCHER)) {
            // every level of this reduces speed by 20%, damage and armour by 15% up to 80% & 60% maximum at level 4.
            // (this is implemented in ModEffects).
            if (amplifier > 3) { amplifier = 3; }
            entity.world.addParticle(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
