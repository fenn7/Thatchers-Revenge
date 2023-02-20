package net.fenn7.thatchermod.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.FirstSpectreEntity;
import net.fenn7.thatchermod.commonside.entity.mobs.ThatcherEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
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
        return new Identifier(ThatcherMod.MOD_ID, "textures/entity/spectre/first_spectre_0.png");
    }

    @Override
    public Identifier getAnimationFileLocation(FirstSpectreEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/first_spectre.animation.json");
    }

    @Override
    public void setCustomAnimations(FirstSpectreEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        if (animatable.age < 40) {
            IBone RArm = this.getAnimationProcessor().getBone("RightArm");
            IBone RTip = this.getAnimationProcessor().getBone("RightArmBladeTip");

            Vec3d basePos = animatable.getPos();
            Vec3d armPit = basePos.add(new Vec3d(RArm.getPivotX(), RArm.getPivotY(), RArm.getPivotZ()).multiply(1.0 / 16.0));
            Vec3d hand = basePos.add(new Vec3d(RTip.getPivotX(), RTip.getPivotY(), RTip.getPivotZ()).multiply(1.0 / 16.0));

            Vec3d hit = hand.subtract(armPit).rotateX(RArm.getRotationX())
                    .rotateY(RArm.getRotationY())
                    .rotateZ(RArm.getRotationZ());

            /*ThatcherMod.LOGGER.warn("BELOW HAS X ROTATION OF: " + RArm.getRotationX() +
                    " YAW: " + animatable.getYaw() * (-Math.PI / 180.0));*/
            ThatcherMod.LOGGER.warn("POS: " + basePos + "AGE: " + animatable.age);
            ThatcherMod.LOGGER.warn("HAND GEO POSSY: " + ((GeoBone) RTip).getWorldPosition().x
                    + ", " + ((GeoBone) RTip).getWorldPosition().y
                    + ", " + ((GeoBone) RTip).getWorldPosition().z
            );
            ThatcherMod.LOGGER.warn("ARM GEO POSSY: " + ((GeoBone) RArm).getWorldPosition().x
                    + ", " + ((GeoBone) RArm).getWorldPosition().y
                    + ", " + ((GeoBone) RArm).getWorldPosition().z
            );
        }
    }

    @Override
    public void codeAnimations(FirstSpectreEntity entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
        super.codeAnimations(entity, uniqueID, customPredicate);
    }
}
