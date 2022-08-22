package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.custom.ParamilitaryEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ParamilitaryModel extends AnimatedGeoModel<ParamilitaryEntity> {
    @Override
    public Identifier getModelResource(ParamilitaryEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/paramilitary.geo.json");
    }

    @Override
    public Identifier getTextureResource(ParamilitaryEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/paramilitary/paramilitary.png");
    }

    @Override
    public Identifier getAnimationResource(ParamilitaryEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/paramilitary.animation.json");
    }
}
