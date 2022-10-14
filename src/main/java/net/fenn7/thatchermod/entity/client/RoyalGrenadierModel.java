package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.mobs.RoyalFencerEntity;
import net.fenn7.thatchermod.entity.mobs.RoyalGrenadierEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RoyalGrenadierModel extends AnimatedGeoModel<RoyalGrenadierEntity> {
    @Override
    public Identifier getModelResource(RoyalGrenadierEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/royal_grenadier.geo.json");
    }

    @Override
    public Identifier getTextureResource(RoyalGrenadierEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_grenadier.png");
    }

    @Override
    public Identifier getAnimationResource(RoyalGrenadierEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/royal_grenadier.animation.json");
    }
}
