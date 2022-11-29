package net.fenn7.thatchermod.entity.client.projectiles;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.projectiles.TrickleDownTridentEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TrickleDownTridentModel extends AnimatedGeoModel<TrickleDownTridentEntity> {
    @Override
    public Identifier getModelLocation(TrickleDownTridentEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/trickle_down_trident.geo.json");
    }

    @Override
    public Identifier getTextureLocation(TrickleDownTridentEntity object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/item/sceptre3d/trickle_down_trident_gold.png");
    }

    @Override
    public Identifier getAnimationFileLocation(TrickleDownTridentEntity animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/trickle_down_trident.animation.json");
    }
}
