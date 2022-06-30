package net.fenn7.thatchermod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.particle.custom.ThatcherJumpParticle;
import net.fenn7.thatchermod.screen.ModScreenHandlers;
import net.fenn7.thatchermod.screen.ThatcherismAltarScreen;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ThatcherModClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.THATCHER_JUMPSCARE, ThatcherJumpParticle.Factory::new);
        ScreenRegistry.register(ModScreenHandlers.THATCHERISM_ALTAR_SCREEN_HANDLER, ThatcherismAltarScreen::new);
    }
}
