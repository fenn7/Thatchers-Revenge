package net.fenn7.thatchermod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.particle.custom.ThatcherJumpParticle;

public class ThatcherModClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.THATCHER_JUMPSCARE, ThatcherJumpParticle.Factory::new);
    }
}
