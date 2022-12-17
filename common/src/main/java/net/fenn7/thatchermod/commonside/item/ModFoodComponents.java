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
                    .statusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 120, 1), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 1200, 9), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200, 9), 1.0F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 9), 1.0F)
                    .build();

    public static final FoodComponent STARGAZY_PIE =
            new FoodComponent.Builder().hunger(16).saturationModifier(20f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400), 0.33F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600), 0.33F)
                    .build();

    public static final FoodComponent FISH_AND_CHIPS =
            new FoodComponent.Builder().hunger(14).saturationModifier(22f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600), 0.5F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200), 0.25F)
                    .build();

    public static final FoodComponent CHIP_BUTTY =
            new FoodComponent.Builder().hunger(8).saturationModifier(15f).snack()
                    .statusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 400), 0.3F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 400), 0.5F)
                    .build();

    public static final FoodComponent STEAK_BAKE =
            new FoodComponent.Builder().hunger(12).saturationModifier(15f).snack()
                    .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 40), 0.1F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300), 0.5F)
                    .build();

    public static final FoodComponent SAUSAGE_ROLL =
            new FoodComponent.Builder().hunger(10).saturationModifier(14f).snack()
                    .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 40), 0.1F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 300), 0.5F)
                    .build();
}
