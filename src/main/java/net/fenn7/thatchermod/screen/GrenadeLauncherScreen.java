package net.fenn7.thatchermod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GrenadeLauncherScreen extends HandledScreen<GrenadeLauncherScreenHandler> {
    private static final String imgPath = "textures/gui/grenade_launcher_gui.png";
    private static final Identifier TEXTURE = new Identifier(ThatcherMod.MOD_ID, imgPath);

    public GrenadeLauncherScreen(GrenadeLauncherScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        titleY += 18 / 2;
        playerInventoryTitleY -= 18 / 2;
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
        int y = (height - backgroundHeight + 18) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
