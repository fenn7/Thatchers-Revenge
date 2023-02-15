package net.fenn7.thatchermod.client.entity;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.AbstractSpectreEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public abstract class AbstractSpectreRenderer extends GeoEntityRenderer<AbstractSpectreEntity> {
    protected final String defaultPath;
    protected final String spectreEntityName;
    protected final String spectreFilePath = "textures/entity/spectre/";
    protected final int transitionStages;
    public AbstractSpectreRenderer(EntityRendererFactory.Context ctx,
                                   AnimatedGeoModel model, String spectreEntityName, int transitionStages) {
        super(ctx, model);
        this.spectreEntityName = spectreEntityName;
        this.defaultPath = this.spectreFilePath + this.spectreEntityName;
        this.transitionStages = transitionStages;
    }

    @Override
    public Identifier getTexture(AbstractSpectreEntity entity) {
        int fadingStage = entity != null ? this.getTransitionStage(entity) : 0;
        return new Identifier(ThatcherMod.MOD_ID,  this.defaultPath + ("_" + fadingStage) + ".png");
    }

    @Override
    public RenderLayer getRenderType(AbstractSpectreEntity entity, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(this.getTexture(entity));
    }

    @Override
    public void render(AbstractSpectreEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (this.getTransitionStage(entity) == this.transitionStages) {
            return;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    protected int getTransitionStage(AbstractSpectreEntity entity) {
        return (int) Math.ceil((double) entity.getIncorporealTicks() / this.transitionStages);
    }
}
