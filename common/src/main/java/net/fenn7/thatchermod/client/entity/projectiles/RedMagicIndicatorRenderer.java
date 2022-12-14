package net.fenn7.thatchermod.client.entity.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.projectiles.RedMagicIndicatorEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RedMagicIndicatorRenderer extends EntityRenderer<RedMagicIndicatorEntity> {
    private static final Identifier TEXTURE = new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/cursed_missile.png");

    public RedMagicIndicatorRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(RedMagicIndicatorEntity entity) {
        return TEXTURE;
    }
}
