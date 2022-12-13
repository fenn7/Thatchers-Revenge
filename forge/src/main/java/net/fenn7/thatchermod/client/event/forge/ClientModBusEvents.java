package net.fenn7.thatchermod.client.event.forge;

import net.fenn7.thatchermod.client.entity.armour.ThatcheriteArmourRenderer;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.item.custom.ThatcheriteArmourItem;
import net.fenn7.thatchermod.commonside.item.custom.forge.ThatcheriteArmourItemImpl;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ThatcherMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(ThatcheriteArmourItem.class, () -> {
            ThatcheriteArmourRenderer renderer = new ThatcheriteArmourRenderer();
            ThatcheriteArmourItemImpl.setModel(renderer.getGeoModelProvider());
            return renderer;
        });
    }
}
