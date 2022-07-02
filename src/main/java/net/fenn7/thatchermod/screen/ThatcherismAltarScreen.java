package net.fenn7.thatchermod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ThatcherismAltarScreen extends HandledScreen<ThatcherismAltarScreenHandler> {
    private static String imgPath = "textures/gui/thatcherism_altar_gui.png";
    private static final Identifier TEXTURE = new Identifier(ThatcherMod.MOD_ID, imgPath);

    public ThatcherismAltarScreen(ThatcherismAltarScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        if (handler.isPrepared()) {
            drawTexture(matrices, x + 61, y + 37, 176, 0, handler.getScaledProgress(), 22);
            drawTexture(matrices, x + 115 - handler.getScaledProgress(), y + 37, 256 - handler.getScaledProgress(),
                    23, handler.getScaledProgress(), 22);
        }
    }

    public void setImgPath(String imagePath){
        imgPath = imagePath;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}

