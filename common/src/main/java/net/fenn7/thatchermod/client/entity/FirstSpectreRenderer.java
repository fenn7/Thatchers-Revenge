package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.FirstSpectreEntity;
import net.fenn7.thatchermod.commonside.entity.mobs.ThatcherEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class FirstSpectreRenderer extends GeoEntityRenderer<FirstSpectreEntity> {
    public FirstSpectreRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FirstSpectreModel());
    }

    @Override
    public Identifier getTexture(FirstSpectreEntity instance) {
        if (instance != null && instance.age % 40 >= 20) {
            return new Identifier(ThatcherMod.MOD_ID, "textures/entity/spectre/second_spectre.png");
        }
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/spectre/first_spectre.png");
    }

    @Override
    public RenderLayer getRenderType(FirstSpectreEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTexture(animatable));
    }

    @Override
    public void render(FirstSpectreEntity animatable, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
