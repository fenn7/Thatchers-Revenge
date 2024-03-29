package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.mobs.RoyalFencerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RoyalFencerModel extends AnimatedGeoModel<RoyalFencerEntity> {
    @Override
    public Identifier getModelResource(RoyalFencerEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/royal_fencer.geo.json");
    }

    @Override
    public Identifier getTextureResource(RoyalFencerEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/military/royal_fencer.png");
    }

    @Override
    public Identifier getAnimationResource(RoyalFencerEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/royal_fencer.animation.json");
    }

    @Override
    public void setLivingAnimations(RoyalFencerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
