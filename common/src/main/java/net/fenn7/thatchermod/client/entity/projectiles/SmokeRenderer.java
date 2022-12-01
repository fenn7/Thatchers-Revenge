package net.fenn7.thatchermod.client.entity.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.projectiles.SmokeEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SmokeRenderer extends EntityRenderer<SmokeEntity> {
    private static final Identifier TEXTURE = new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/cursed_missile.png");

    public SmokeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SmokeEntity entity) {
        return TEXTURE;
    }
}
