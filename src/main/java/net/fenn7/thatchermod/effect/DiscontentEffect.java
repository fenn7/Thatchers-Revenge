package net.fenn7.thatchermod.effect;

import net.fenn7.thatchermod.particle.ModParticles;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class DiscontentEffect extends StatusEffect {
    private static final Block iceBlock = Blocks.BLUE_ICE;

    protected DiscontentEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setInPowderSnow(true);
        entity.setFrozenTicks(140);
        if (!entity.world.isClient) {
            ((ServerWorld)entity.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, iceBlock.getDefaultState()),
                    entity.getX(), entity.getY() + 0.5D, entity.getZ(), 1,
                    0, 0, 0, 0);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) { return true; }
}
