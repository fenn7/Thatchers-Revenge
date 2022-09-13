package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.entity.projectiles.SmokeGrenadeEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SmokeGrenadeRenderer extends GeoProjectilesRenderer<SmokeGrenadeEntity> {
    private boolean isSmoking;

    public SmokeGrenadeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SmokeGrenadeModel());
    }

    @Override
    public boolean shouldRender(SmokeGrenadeEntity entity, Frustum frustum, double x, double y, double z) {
        this.isSmoking = entity.isSmoking();
        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public void render(SmokeGrenadeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        if (!this.isSmoking) {
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }
}
