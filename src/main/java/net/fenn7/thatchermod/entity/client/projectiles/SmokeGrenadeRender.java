package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.entity.projectiles.SmokeGrenadeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SmokeGrenadeRender extends GeoProjectilesRenderer<SmokeGrenadeEntity> {
    public SmokeGrenadeRender(EntityRendererFactory.Context ctx) {
        super(ctx, new SmokeGrenadeModel());
    }
}
