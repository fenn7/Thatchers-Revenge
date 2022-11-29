package net.fenn7.thatchermod.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {

    public static final DefaultParticleType THATCHER_JUMPSCARE = FabricParticleTypes.simple();
    public static final DefaultParticleType DEFICIENCY_INDICATOR = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE,
                new Identifier(ThatcherMod.MOD_ID, "thatcher_jumpscare"), THATCHER_JUMPSCARE);
        Registry.register(Registry.PARTICLE_TYPE,
                new Identifier(ThatcherMod.MOD_ID, "deficiency_indicator"), DEFICIENCY_INDICATOR);
    }
}

