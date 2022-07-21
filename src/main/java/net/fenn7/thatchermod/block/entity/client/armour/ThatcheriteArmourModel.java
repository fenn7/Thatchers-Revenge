package net.fenn7.thatchermod.block.entity.client.armour;

import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.item.custom.ThatcheriteArmourItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ThatcheriteArmourModel extends AnimatedGeoModel<ThatcheriteArmourItem> {
    @Override
    public Identifier getModelResource(ThatcheriteArmourItem object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/thatcherite_armour_black.geo.json");
    }

    @Override
    public Identifier getTextureResource(ThatcheriteArmourItem object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/models/armour/thatcherite_armour_texture.png");
    }

    @Override
    public Identifier getAnimationResource(ThatcheriteArmourItem animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/thatcherite_armour_animation.json");
    }
}
