package net.fenn7.thatchermod.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.effect.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderEvents {

    private static final Identifier LAST_STAND_OVERLAY = new Identifier(ThatcherMod.MOD_ID, "textures/misc/last_stand.png");

    public static void afterRender() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.hasStatusEffect(ModEffects.LAST_STAND.get())) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            renderOverlay(height, width);
        }
    }

    private static void renderOverlay(float scaledHeight, float scaledWidth) {
        float opacity = 0.6F;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        RenderSystem.setShaderTexture(0, LAST_STAND_OVERLAY);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
