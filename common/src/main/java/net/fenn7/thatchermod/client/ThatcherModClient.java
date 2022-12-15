package net.fenn7.thatchermod.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.client.entity.RoyalFencerRenderer;
import net.fenn7.thatchermod.client.entity.RoyalGrenadierRenderer;
import net.fenn7.thatchermod.client.entity.ThatcherModelRenderer;
import net.fenn7.thatchermod.client.entity.projectiles.*;
import net.fenn7.thatchermod.client.event.KeyInputs;
import net.fenn7.thatchermod.client.network.ModPacketsClient;
import net.fenn7.thatchermod.client.particle.custom.DeficiencyIndicatorParticle;
import net.fenn7.thatchermod.client.particle.custom.ThatcherJumpParticle;
import net.fenn7.thatchermod.client.screen.GrenadeLauncherScreen;
import net.fenn7.thatchermod.client.screen.ThatcherismAltarScreen;
import net.fenn7.thatchermod.client.sound.ModSounds;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.particle.ModParticles;
import net.fenn7.thatchermod.commonside.screen.ModScreenHandlers;
import net.fenn7.thatchermod.client.util.ModPredicates;

@Environment(EnvType.CLIENT)
public class ThatcherModClient {

    public static void init() {
        ModSounds.registerModSounds();

        ParticleProviderRegistry.register(ModParticles.THATCHER_JUMPSCARE, ThatcherJumpParticle.Factory::new);
        ParticleProviderRegistry.register(ModParticles.DEFICIENCY_INDICATOR, DeficiencyIndicatorParticle.Factory::new);

        ModScreenHandlers.THATCHERISM_ALTAR_SCREEN_HANDLER.listen(screenHandlerType -> MenuRegistry.registerScreenFactory(
                screenHandlerType,
                ThatcherismAltarScreen::new
        ));
        ModScreenHandlers.GRENADE_LAUNCHER_SCREEN_HANDLER.listen(screenHandlerType -> MenuRegistry.registerScreenFactory(
                screenHandlerType,
                GrenadeLauncherScreen::new
        ));

        EntityRendererRegistry.register(ModEntities.THATCHER, ThatcherModelRenderer::new);
        EntityRendererRegistry.register(ModEntities.ROYAL_FENCER, RoyalFencerRenderer::new);
        EntityRendererRegistry.register(ModEntities.ROYAL_GRENADIER, RoyalGrenadierRenderer::new);

        EntityRendererRegistry.register(ModEntities.CURSED_METEOR, CursedMeteorRenderer::new);
        EntityRendererRegistry.register(ModEntities.CURSED_MISSILE, CursedMissileRenderer::new);
        EntityRendererRegistry.register(ModEntities.SMOKE_ENTITY, SmokeRenderer::new);
        EntityRendererRegistry.register(ModEntities.RED_MAGIC_ENTITY, RedMagicIndicatorRenderer::new);
        EntityRendererRegistry.register(ModEntities.GRENADE_ENTITY, GrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIRE_GRENADE_ENTITY, FireGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.SMOKE_GRENADE_ENTITY, SmokeGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.TRICKLE_DOWN_TRIDENT_ENTITY, TrickleDownTridentRenderer::new);

        KeyInputs.register();
        ModPacketsClient.registerS2CPackets();
        ModPredicates.registerAllPredicates();
    }
}
