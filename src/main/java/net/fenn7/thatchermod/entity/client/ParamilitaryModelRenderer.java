package net.fenn7.thatchermod.entity.client;

import com.google.common.collect.Lists;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.custom.ParamilitaryEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ParamilitaryModelRenderer extends GeoEntityRenderer<ParamilitaryEntity> {
    private VertexConsumerProvider rtb;
    private Identifier whTexture;
    private boolean isAttacking;

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

    @Override
    public void renderEarly(ParamilitaryEntity animatable, MatrixStack stackIn, float ticks,
                            VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                            int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.isAttacking = animatable.isAttacking();
        this.rtb = renderTypeBuffer;
        this.whTexture = this.getTextureResource(animatable);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn,
                                  int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("RightHand") && this.isAttacking) {
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(bone.getRotationX() - 80));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(bone.getRotationY()));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(bone.getRotationZ()));
            stack.translate(0.37D, 0.30D, 0.74D);
            stack.scale(1.0f, 1.2f, 0.75f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(mainHand, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND,
                    packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        else if (bone.getName().equals("Body") && !this.isAttacking) {
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(bone.getRotationX()));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(bone.getRotationY()));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(bone.getRotationZ() - 90));
            stack.translate(-1.25D, 0.0D, 0.15D);
            stack.scale(0.7f, 0.7f, 0.7f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(mainHand, ModelTransformation.Mode.NONE,
                    packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
