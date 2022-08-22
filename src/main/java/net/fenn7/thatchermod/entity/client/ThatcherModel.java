package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.custom.ThatcherEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ThatcherModel extends AnimatedGeoModel<ThatcherEntity> {
    @Override
    public Identifier getModelResource(ThatcherEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/thatcher.geo.json");
    }

    @Override
    public Identifier getTextureResource(ThatcherEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/thatcher.png");
    }

    @Override
    public Identifier getAnimationResource(ThatcherEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/thatcher.animation.json");
    }

    @Override
    public void setLivingAnimations(ThatcherEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
