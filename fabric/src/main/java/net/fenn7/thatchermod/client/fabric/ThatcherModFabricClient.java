package net.fenn7.thatchermod.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fenn7.thatchermod.client.ThatcherModClient;
import net.fenn7.thatchermod.client.entity.armour.ThatcheriteArmourRenderer;
import net.fenn7.thatchermod.client.event.RenderEvents;
import net.fenn7.thatchermod.commonside.item.ModItems;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class ThatcherModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ThatcherModClient.init();
        registerGeoArmorRenderers();
        WorldRenderEvents.END.register(context -> RenderEvents.afterRender());
    }

    private static void registerGeoArmorRenderers() {
        GeoArmorRenderer.registerArmorRenderer(
                new ThatcheriteArmourRenderer(),
                ModItems.THATCHERITE_HELMET.get(),
                ModItems.THATCHERITE_CHESTPLATE.get(),
                ModItems.THATCHERITE_GREAVES.get(),
                ModItems.THATCHERITE_BOOTS.get()
        );
    }
}
