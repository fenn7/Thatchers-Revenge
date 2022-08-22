package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.custom.SmokeEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class SmokeRenderer extends EntityRenderer<SmokeEntity> {
    private static final Identifier TEXTURE = new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/cursed_missile.png");

    public SmokeRenderer(EntityRendererFactory.Context ctx) { super(ctx); }

    @Override
    public Identifier getTexture(SmokeEntity entity) {
        return TEXTURE;
    }
}
