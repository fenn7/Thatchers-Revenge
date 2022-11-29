package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GrenadeModel extends AnimatedGeoModel<GrenadeEntity> {

    @Override
    public Identifier getModelLocation(GrenadeEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/grenade.geo.json");
    }

    @Override
    public Identifier getTextureLocation(GrenadeEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/grenade3d/grenade.png");
    }

    @Override
    public Identifier getAnimationFileLocation(GrenadeEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/grenade.animation.json");
    }
}
