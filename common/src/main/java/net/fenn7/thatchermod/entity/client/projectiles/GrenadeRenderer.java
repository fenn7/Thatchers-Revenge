package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class GrenadeRenderer extends GeoProjectilesRenderer<GrenadeEntity> {
    public GrenadeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GrenadeModel());
    }
}
