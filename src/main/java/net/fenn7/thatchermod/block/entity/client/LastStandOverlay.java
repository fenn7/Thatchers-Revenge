package net.fenn7.thatchermod.block.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.effect.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LastStandOverlay implements HudRenderCallback {
    public static final Identifier LAST_STAND = new Identifier(ThatcherMod.MOD_ID, "textures/misc/last_stand.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player.hasStatusEffect(ModEffects.LAST_STAND)) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int x = width / 2;
            int y = height;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.1F);
            RenderSystem.setShaderTexture(0, LAST_STAND);
            DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, width, height,
                        width, height);
        }
    }
}
