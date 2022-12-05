package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.ThatcherEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class ThatcherModel extends AnimatedGeoModel<ThatcherEntity> {
    @Override
    public Identifier getModelLocation(ThatcherEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/thatcher.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ThatcherEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/thatcher/thatcher.png");
    }

    @Override
    public Identifier getAnimationFileLocation(ThatcherEntity animatable) {
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