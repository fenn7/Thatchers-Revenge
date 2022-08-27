package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.CursedMissileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class CursedMissileRenderer extends EntityRenderer<CursedMissileEntity> {
    private static final Identifier TEXTURE = new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/cursed_missile.png");
    private static final RenderLayer LAYER;

    public CursedMissileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    protected int getBlockLight(CursedMissileEntity missileEntity, BlockPos blockPos) {
        return 10;
    }

    public void render(CursedMissileEntity missileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 1, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 1, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 2, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 2, 0, 0);
        matrixStack.pop();
        super.render(missileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5F, (float)y - 0.25F, 0.0F).color(255, 255, 255, 255).texture((float)textureU, (float)textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    @Override
    public Identifier getTexture(CursedMissileEntity entity) {
        return TEXTURE;
    }

    static {
        LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    }
}
