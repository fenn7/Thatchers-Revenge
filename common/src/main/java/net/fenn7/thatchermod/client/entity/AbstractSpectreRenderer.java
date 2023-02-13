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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSpectreRenderer extends GeoEntityRenderer<AbstractSpectreEntity> {
    protected final String spectreEntityName;
    protected final String spectreFilePath = "textures/entity/spectre/";
    protected final int transitionStages;
    public AbstractSpectreRenderer(EntityRendererFactory.Context ctx,
                                   AnimatedGeoModel<AbstractSpectreEntity> model, String spectreEntityName, int transitionStages) {
        super(ctx, model);
        this.spectreEntityName = spectreEntityName;
        this.transitionStages = transitionStages;
        this.regenerateFadedTextures(transitionStages);
    }

    @Override
    public Identifier getTexture(AbstractSpectreEntity entity) {
        int fadingStage = entity != null ? this.getTransitionStage(entity) : 0;
        return new Identifier(ThatcherMod.MOD_ID,  this.spectreFilePath + this.spectreEntityName +
                (fadingStage == 0 ? "" : "_" + fadingStage) + ".png");
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

    protected void regenerateFadedTextures(int numberOfTransitions) {
        String imgPath = this.spectreFilePath + this.spectreEntityName;
        File imgFile = new File(imgPath + ".png");
        try {
            BufferedImage image = ImageIO.read(imgFile);
            int width = image.getWidth();
            int height = image.getHeight();
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int pixelsToRemove = width * height / numberOfTransitions;

            for (int i = 0; i < numberOfTransitions - 1; i++) {
                for (int j = 0; j < pixelsToRemove; j++) {
                    int x = random.nextInt(width);
                    int y = random.nextInt(height);
                    image.setRGB(x, y, 0x00000000);
                }
                File newFile = new File(imgPath + "_" + (i + 1) + ".png");
                ImageIO.write(image, "png", newFile);
                image = ImageIO.read(newFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
