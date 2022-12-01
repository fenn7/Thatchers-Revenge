package net.fenn7.thatchermod.client.entity.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.entity.projectiles.SmokeGrenadeEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

@Environment(EnvType.CLIENT)
public class SmokeGrenadeRenderer extends GeoProjectilesRenderer<SmokeGrenadeEntity> {
    private boolean isSmoking;

    public SmokeGrenadeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SmokeGrenadeModel());
    }

    @Override
    public boolean shouldRender(SmokeGrenadeEntity entity, Frustum frustum, double x, double y, double z) {
        // this.isSmoking = entity.isSmoking();
        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public void renderEarly(SmokeGrenadeEntity animatable, MatrixStack stackIn, float partialTicks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        //this.isSmoking = animatable.isSmoking();
        super.renderEarly(animatable, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void render(SmokeGrenadeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        if (!entityIn.isSmoking()) {
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }
}
