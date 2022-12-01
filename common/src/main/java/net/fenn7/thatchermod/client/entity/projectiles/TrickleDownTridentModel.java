package net.fenn7.thatchermod.client.entity.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.projectiles.TrickleDownTridentEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
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
