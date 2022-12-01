package net.fenn7.thatchermod.particle;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

public class ModParticles {

    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.PARTICLE_TYPE_KEY);

    public static final RegistrySupplier<DefaultParticleType> THATCHER_JUMPSCARE = PARTICLE_TYPES.register(
            "thatcher_jumpscare",
            ModParticles::createParticle
    );

    public static final RegistrySupplier<DefaultParticleType> DEFICIENCY_INDICATOR = PARTICLE_TYPES.register(
            "deficiency_indicator",
            ModParticles::createParticle
    );

    public static void registerParticles() {
        PARTICLE_TYPES.register();
    }

    @ExpectPlatform
    public static DefaultParticleType createParticle() {
        throw new AssertionError();
    }
}
