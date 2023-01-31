package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.SecondSpectreEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class SecondSpectreModel extends AnimatedGeoModel<SecondSpectreEntity> {
    @Override
    public Identifier getModelLocation(SecondSpectreEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/second_spectre.geo.json");
    }

    @Override
    public Identifier getTextureLocation(SecondSpectreEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/spectre/second_spectre.png");
    }

    @Override
    public Identifier getAnimationFileLocation(SecondSpectreEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/second_spectre.animation.json");
    }
}
