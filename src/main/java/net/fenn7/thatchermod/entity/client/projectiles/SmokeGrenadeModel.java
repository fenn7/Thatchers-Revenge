package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.SmokeGrenadeEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SmokeGrenadeModel extends AnimatedGeoModel<SmokeGrenadeEntity> {
    @Override
    public Identifier getModelResource(SmokeGrenadeEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/grenade_smoke.geo.json");
    }

    @Override
    public Identifier getTextureResource(SmokeGrenadeEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/grenade3d/grenade_smoke.png");
    }

    @Override
    public Identifier getAnimationResource(SmokeGrenadeEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/grenade.animation.json");
    }
}
