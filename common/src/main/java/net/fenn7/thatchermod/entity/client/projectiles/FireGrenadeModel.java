package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.FireGrenadeEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FireGrenadeModel extends AnimatedGeoModel<FireGrenadeEntity> {

    @Override
    public Identifier getModelLocation(FireGrenadeEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/grenade_fire.geo.json");
    }

    @Override
    public Identifier getTextureLocation(FireGrenadeEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/grenade3d/grenade_fire.png");
    }

    @Override
    public Identifier getAnimationFileLocation(FireGrenadeEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/grenade.animation.json");
    }
}
