package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.RoyalFencerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class RoyalFencerModel extends AnimatedGeoModel<RoyalFencerEntity> {
    @Override
    public Identifier getModelLocation(RoyalFencerEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/royal_fencer.geo.json");
    }

    @Override
    public Identifier getTextureLocation(RoyalFencerEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_fencer.png");
    }

    @Override
    public Identifier getAnimationFileLocation(RoyalFencerEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/royal_fencer.animation.json");
    }


    @Override
    public void setCustomAnimations(RoyalFencerEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("Head");

        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
