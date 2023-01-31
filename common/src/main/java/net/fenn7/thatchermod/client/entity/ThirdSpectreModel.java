package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.ThirdSpectreEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class ThirdSpectreModel extends AnimatedGeoModel<ThirdSpectreEntity> {
    @Override
    public Identifier getModelLocation(ThirdSpectreEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/third_spectre.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ThirdSpectreEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/spectre/third_spectre.png");
    }

    @Override
    public Identifier getAnimationFileLocation(ThirdSpectreEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/third_spectre.animation.json");
    }
}
