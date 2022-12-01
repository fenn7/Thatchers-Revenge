package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.RoyalGrenadierEntity;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class RoyalGrenadierRenderer extends GeoEntityRenderer<RoyalGrenadierEntity> {
    private VertexConsumerProvider rtb;
    private Identifier whTexture;
    private boolean isShooting;
    private boolean hasSmoked;

    public RoyalGrenadierRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RoyalGrenadierModel());
    }

    @Override
    public Identifier getTextureLocation(RoyalGrenadierEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_grenadier.png");
    }

    @Override
    public Identifier getTexture(RoyalGrenadierEntity instance) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_grenadier.png");
    }

    @Override
    public void renderEarly(RoyalGrenadierEntity animatable, MatrixStack stackIn, float ticks,
                            VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                            int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.whTexture = this.getTextureLocation(animatable);
        this.isShooting = animatable.isAttacking();
        this.hasSmoked = animatable.hasUsedSmoke();
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("RightHand") && this.isShooting) {
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(bone.getRotationX() - 80));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(bone.getRotationY()));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(bone.getRotationZ()));
            stack.translate(0.37D, 0.30D, 0.74D);
            stack.scale(0.75f, 0.75f, 0.75f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(mainHand, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND,
                    packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        if (bone.getName().equals("LeftLeg")) {
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(bone.getRotationX()));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(bone.getRotationY() - 85));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(bone.getRotationZ()));
            stack.translate(0.0D, 0.8D, 0.35D);
            stack.scale(0.75f, 0.75f, 0.75f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.GRENADE.get()),
                    ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        if (bone.getName().equals("RightLeg")) {
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(bone.getRotationX()));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(bone.getRotationY() - 95));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(bone.getRotationZ()));
            stack.translate(0.0D, 0.8D, -0.25D);
            stack.scale(0.75f, 0.75f, 0.75f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.GRENADE_SMOKE.get()),
                    ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
