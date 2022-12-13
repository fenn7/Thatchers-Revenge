package net.fenn7.thatchermod.client.entity.armour;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.item.custom.ThatcheriteArmourItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class ThatcheriteArmourModel extends AnimatedGeoModel<ThatcheriteArmourItem> {

    @Override
    public Identifier getModelLocation(ThatcheriteArmourItem object) {
        return new Identifier(ThatcherMod.MOD_ID, "geo/thatcherite_armour_black.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ThatcheriteArmourItem object) {
        return new Identifier(ThatcherMod.MOD_ID, "textures/models/armour/thatcherite_armour_texture.png");
    }

    @Override
    public Identifier getAnimationFileLocation(ThatcheriteArmourItem animatable) {
        return new Identifier(ThatcherMod.MOD_ID, "animations/thatcherite_armour_animation.json");
    }
}
