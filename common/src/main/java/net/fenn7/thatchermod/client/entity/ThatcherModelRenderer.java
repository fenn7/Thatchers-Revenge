package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.ThatcherEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ThatcherModelRenderer extends GeoEntityRenderer<ThatcherEntity> {
    public ThatcherModelRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ThatcherModel());
    }

    @Override
    public Identifier getTextureLocation(ThatcherEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/thatcher.png");
    }

    @Override
    public Identifier getTexture(ThatcherEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/thatcher.png");
    }
}
