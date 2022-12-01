package net.fenn7.thatchermod;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.entity.client.RoyalFencerRenderer;
import net.fenn7.thatchermod.entity.client.RoyalGrenadierRenderer;
import net.fenn7.thatchermod.entity.client.ThatcherModelRenderer;
import net.fenn7.thatchermod.entity.client.armour.ThatcheriteArmourRenderer;
import net.fenn7.thatchermod.entity.client.projectiles.*;
import net.fenn7.thatchermod.event.KeyInputs;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.network.ModPackets;
import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.particle.custom.DeficiencyIndicatorParticle;
import net.fenn7.thatchermod.particle.custom.ThatcherJumpParticle;
import net.fenn7.thatchermod.screen.GrenadeLauncherScreen;
import net.fenn7.thatchermod.screen.ModScreenHandlers;
import net.fenn7.thatchermod.screen.ThatcherismAltarScreen;
import net.fenn7.thatchermod.sound.ModSounds;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

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
        EntityRendererRegistry.register(ModEntities.GRENADE_ENTITY, GrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIRE_GRENADE_ENTITY, FireGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.SMOKE_GRENADE_ENTITY, SmokeGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.TRICKLE_DOWN_TRIDENT_ENTITY, TrickleDownTridentRenderer::new);

        KeyInputs.register();
        ModPackets.registerS2CPackets();
    }

    public static void registerGeoArmorRenderers() {
        GeoArmorRenderer.registerArmorRenderer(
                new ThatcheriteArmourRenderer(),
                ModItems.THATCHERITE_HELMET.get(),
                ModItems.THATCHERITE_CHESTPLATE.get(),
                ModItems.THATCHERITE_GREAVES.get(),
                ModItems.THATCHERITE_BOOTS.get()
        );
    }
}
