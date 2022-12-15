package net.fenn7.thatchermod.mixin.commonloader.commonside.accessor;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatusEffectInstance.class)
public interface StatusEffectInstanceAccessor {

    @Accessor("duration")
    void thatchersRevenge$setDuration(int duration);

    @Accessor("amplifier")
    void thatchersRevenge$setAmplifier(int amplifier);
}
