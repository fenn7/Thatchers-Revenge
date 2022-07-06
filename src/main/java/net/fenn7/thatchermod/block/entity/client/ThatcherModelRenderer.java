package net.fenn7.thatchermod.block.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.block.entity.custom.ThatcherEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ThatcherModelRenderer extends GeoEntityRenderer<ThatcherEntity> {
    public ThatcherModelRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ThatcherModel());
    }

    @Override
    public Identifier getTextureResource(ThatcherEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/thatcher.png");
    }
}
