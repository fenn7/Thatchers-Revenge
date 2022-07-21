package net.fenn7.thatchermod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fenn7.thatchermod.block.entity.ModEntities;
import net.fenn7.thatchermod.block.entity.client.CursedMeteorRenderer;
import net.fenn7.thatchermod.block.entity.client.CursedMissileRenderer;
import net.fenn7.thatchermod.block.entity.client.ThatcherModelRenderer;
import net.fenn7.thatchermod.block.entity.client.armour.ThatcheriteArmourRenderer;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.particle.custom.DeficiencyIndicatorParticle;
import net.fenn7.thatchermod.particle.custom.ThatcherJumpParticle;
import net.fenn7.thatchermod.screen.ModScreenHandlers;
import net.fenn7.thatchermod.screen.ThatcherismAltarScreen;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ThatcherModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.THATCHER_JUMPSCARE, ThatcherJumpParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.DEFICIENCY_INDICATOR, DeficiencyIndicatorParticle.Factory::new);
        ScreenRegistry.register(ModScreenHandlers.THATCHERISM_ALTAR_SCREEN_HANDLER, ThatcherismAltarScreen::new);
        EntityRendererRegistry.register(ModEntities.THATCHER, ThatcherModelRenderer::new);
        EntityRendererRegistry.register(ModEntities.CURSED_METEOR, CursedMeteorRenderer::new);
        EntityRendererRegistry.register(ModEntities.CURSED_MISSILE, CursedMissileRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(new ThatcheriteArmourRenderer(), ModItems.THATCHERITE_BOOTS,
                ModItems.THATCHERITE_GREAVES, ModItems.THATCHERITE_CHESTPLATE, ModItems.THATCHERITE_HELMET);
    }
}
