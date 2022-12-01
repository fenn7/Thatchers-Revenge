package net.fenn7.thatchermod.client.entity.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.entity.projectiles.FireGrenadeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

@Environment(EnvType.CLIENT)
public class FireGrenadeRenderer extends GeoProjectilesRenderer<FireGrenadeEntity> {
    public FireGrenadeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FireGrenadeModel());
    }
}
