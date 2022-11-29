package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.entity.projectiles.TrickleDownTridentEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrickleDownTridentRenderer extends GeoProjectilesRenderer<TrickleDownTridentEntity> {
    public static final List<Color> RAINBOW = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.ofRGB(130, 200, 230), Color.ofRGB(100, 100, 180), Color.ofRGB(70, 0, 130));
    private final AnimatedGeoModel<TrickleDownTridentEntity> modelProvider = new TrickleDownTridentModel();
    private int ticks = 0;

    public TrickleDownTridentRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new TrickleDownTridentModel());
    }

    @Override
    public void render(TrickleDownTridentEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
                       VertexConsumerProvider bufferIn, int packedLightIn) {
        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(entityIn));
        matrixStackIn.push();
        matrixStackIn.multiply(Vec3f.POSITIVE_Y
                .getDegreesQuaternion(MathHelper.lerp(partialTicks, entityIn.prevYaw, entityIn.getYaw()) - 90.0F));
        matrixStackIn.multiply(Vec3f.POSITIVE_Z
                .getDegreesQuaternion(MathHelper.lerp(partialTicks, entityIn.prevPitch, entityIn.getPitch()) + 90.0F));
        MinecraftClient.getInstance().getTextureManager().bindTexture(getTexture(entityIn));
        Color renderColor = getRenderColor(entityIn, partialTicks, matrixStackIn, bufferIn, null, packedLightIn);
        RenderLayer renderType = getRenderType(entityIn, partialTicks, matrixStackIn, bufferIn, null, packedLightIn,
                getTexture(entityIn));

        if (entityIn.isEnchanted()) {
            int i = (ticks / 15);
            if (i > RAINBOW.size() - 1) {
                i = 0;
                ticks = 0;
            }
            renderColor = RAINBOW.get(i);
            ++ticks;
        }
        render(model, entityIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn,
                getPackedOverlay(entityIn, 0), (float) renderColor.getRed() / 255f,
                (float) renderColor.getGreen() / 255f, (float) renderColor.getBlue() / 255f,
                (float) renderColor.getAlpha() / 255);

        float lastLimbDistance = 0.0F;
        float limbSwing = 0.0F;
        EntityModelData entityModelData = new EntityModelData();

        AnimationEvent<TrickleDownTridentEntity> predicate = new AnimationEvent<TrickleDownTridentEntity>(entityIn, limbSwing, lastLimbDistance, partialTicks,
                !(lastLimbDistance > -0.15F && lastLimbDistance < 0.15F), Collections.singletonList(entityModelData));
        ((IAnimatableModel<TrickleDownTridentEntity>) modelProvider).setLivingAnimations(entityIn, this.getUniqueID(entityIn), predicate);
        matrixStackIn.pop();
    }
}
