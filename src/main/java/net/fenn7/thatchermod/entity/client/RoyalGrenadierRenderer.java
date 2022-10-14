package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.mobs.RoyalGrenadierEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RoyalGrenadierRenderer extends GeoEntityRenderer<RoyalGrenadierEntity> {
    public RoyalGrenadierRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RoyalGrenadierModel());
    }

    @Override
    public Identifier getTextureResource(RoyalGrenadierEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_grenadier.png");
    }

    @Override
    public Identifier getTexture(RoyalGrenadierEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_grenadier.png");
    }
}
