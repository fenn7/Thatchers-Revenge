package net.fenn7.thatchermod.entity.client;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.mobs.ParamilitaryEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

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

    @Override
    public void setLivingAnimations(ParamilitaryEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
