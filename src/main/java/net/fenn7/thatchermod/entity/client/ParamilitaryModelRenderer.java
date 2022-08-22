package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.custom.ParamilitaryEntity;
import net.fenn7.thatchermod.entity.custom.ThatcherEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ParamilitaryModelRenderer extends GeoEntityRenderer<ParamilitaryEntity> {
    public ParamilitaryModelRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ParamilitaryModel());
    }

    @Override
    public Identifier getTextureResource(ParamilitaryEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/paramilitary/paramilitary.png");
    }

    @Override
    public Identifier getTexture(ParamilitaryEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/paramilitary/paramilitary.png");
    }
}
