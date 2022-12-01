package net.fenn7.thatchermod.commonside.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {

    public static final FoodComponent HEART_OF_THATCHER =
            new FoodComponent.Builder().hunger(0).saturationModifier(0f).alwaysEdible().meat()
                    .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 0), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 3600, 9), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 3600, 9), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 3600, 4), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3600, 4), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3600, 4), 1.0F)
                    .build();

    public static final FoodComponent SOUL_OF_THATCHER =
            new FoodComponent.Builder().hunger(0).saturationModifier(0f).alwaysEdible()
                    .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 0), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1200, 3), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 1200, 1), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 1200, 9), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200, 9), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 9), 1.0F)
                    .build();
}
