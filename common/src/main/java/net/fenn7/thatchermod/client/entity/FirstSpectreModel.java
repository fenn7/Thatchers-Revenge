package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.FirstSpectreEntity;
import net.fenn7.thatchermod.commonside.entity.mobs.ThatcherEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class FirstSpectreModel extends AnimatedGeoModel<FirstSpectreEntity> {
    @Override
    public Identifier getModelLocation(FirstSpectreEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/first_spectre.geo.json");
    }

    @Override
    public Identifier getTextureLocation(FirstSpectreEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/spectre/first_spectre.png");
    }

    @Override
    public Identifier getAnimationFileLocation(FirstSpectreEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/first_spectre.animation.json");
    }
}
