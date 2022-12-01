package net.fenn7.thatchermod.entity.client.armour;

import net.fenn7.thatchermod.item.custom.ThatcheriteArmourItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ThatcheriteArmourRenderer extends GeoArmorRenderer<ThatcheriteArmourItem> {
    public ThatcheriteArmourRenderer() {
        super(new ThatcheriteArmourModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }
}
