package net.fenn7.thatchermod.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class RenderEvents implements WorldRenderEvents.End {
    private static final Identifier LAST_STAND_OVERLAY = new Identifier("textures/misc/last_stand.png");

    @Override
    public void onEnd(WorldRenderContext context) {
            List<AbstractClientPlayerEntity> playerList = context.world().getPlayers();
            for (PlayerEntity player : playerList) {
                if (player.hasStatusEffect(ModEffects.LAST_STAND)) {

                    // try https://forums.minecraftforge.net/topic/111422-custom-screen-overlay-1165/

                    /*int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
                    int height = MinecraftClient.getInstance().getWindow().getScaledHeight();

                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE,
                            GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE);
                    RenderSystem.setShaderColor(1.0f, 0, 0, 1.0F);
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(1, LAST_STAND_OVERLAY);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferBuilder = tessellator.getBuffer();
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                    bufferBuilder.vertex(0, height, -90.0D).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(width, height, -90.0D).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(width, 0, -90.0D).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(0, 0, -90.0D).texture(0.0F, 0.0F).next();
                    bufferBuilder.color(255, 0, 0, 1.0F);
                    tessellator.draw();
                    RenderSystem.setShaderColor(1.0F, 0F, 0F, 1.0F);
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.disableBlend();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();

                    int i = MinecraftClient.getInstance().getWindow().getScaledWidth();
                    int j = MinecraftClient.getInstance().getWindow().getScaledHeight();
                    float distortionStrength = 1.21213092F;

                    double d = MathHelper.lerp((double)distortionStrength, 2.0D, 1.0D);
                    float f = 0.2F * distortionStrength;
                    float g = 0.4F * distortionStrength;
                    float h = 0.2F * distortionStrength;
                    double e = (double)i * d;
                    double k = (double)j * d;
                    double l = ((double)i - e) / 2.0D;
                    double m = ((double)j - k) / 2.0D;
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE);
                    RenderSystem.setShaderColor(f, g, h, 1.0F);
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, LAST_STAND_OVERLAY);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferBuilder = tessellator.getBuffer();
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                    bufferBuilder.vertex(l, m + k, -90.0D).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(l + e, m + k, -90.0D).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(l + e, m, -90.0D).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(l, m, -90.0D).texture(0.0F, 0.0F).next();
                    tessellator.draw();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.disableBlend();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    BackgroundRenderer.applyFog(MinecraftClient.getInstance().gameRenderer.getCamera(), BackgroundRenderer.FogType.FOG_TERRAIN,
                            20F, true, 0);
                    BackgroundRenderer.setFogBlack();*/
            }
        }
    }
}
