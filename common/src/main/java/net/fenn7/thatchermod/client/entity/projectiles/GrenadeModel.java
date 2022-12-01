package net.fenn7.thatchermod.client.entity.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.projectiles.GrenadeEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
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
