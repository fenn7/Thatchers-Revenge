package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.entity.projectiles.FireGrenadeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FireGrenadeRenderer extends GeoProjectilesRenderer<FireGrenadeEntity> {
    public FireGrenadeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FireGrenadeModel());
    }
}
