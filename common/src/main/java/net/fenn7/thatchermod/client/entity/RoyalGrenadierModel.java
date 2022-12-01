package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.RoyalGrenadierEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class RoyalGrenadierModel extends AnimatedGeoModel<RoyalGrenadierEntity> {
    @Override
    public Identifier getModelLocation(RoyalGrenadierEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/royal_grenadier.geo.json");
    }

    @Override
    public Identifier getTextureLocation(RoyalGrenadierEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_grenadier.png");
    }

    @Override
    public Identifier getAnimationFileLocation(RoyalGrenadierEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/royal_grenadier.animation.json");
    }
}
