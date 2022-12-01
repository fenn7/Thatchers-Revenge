package net.fenn7.thatchermod.particle.fabric;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;

public class ModParticlesImpl {

    public static DefaultParticleType createParticle() {
        return FabricParticleTypes.simple();
    }
}
