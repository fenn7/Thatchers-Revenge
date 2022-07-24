package net.fenn7.thatchermod.effect;

import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Modifier;
import java.util.UUID;

public class ModEffects {

    public static final StatusEffect CALCIUM_DEFICIENCY = Registry.register(Registry.STATUS_EFFECT,
            new Identifier(ThatcherMod.MOD_ID, "calcium_deficiency"),
            new CalciumDeficiencyEffect(StatusEffectCategory.HARMFUL, 15856113)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "31cc06b5-8c33-4ff1-a714-3fd4ee86b2bd",
                    -0.15D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL))
            .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "9dcbf6f0-61df-4c99-9727-7acdc01812b9",
                    -0.15D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "a311afcd-a357-4ce6-b725-f4ffdd6624ad",
                    -0.2D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect STATIC_BUILDUP = Registry.register(Registry.STATUS_EFFECT,
            new Identifier(ThatcherMod.MOD_ID, "static_buildup"),
            new StaticBuildupEffect(StatusEffectCategory.NEUTRAL, 0));
    public static final StatusEffect LAST_STAND = Registry.register(Registry.STATUS_EFFECT,
            new Identifier(ThatcherMod.MOD_ID, "last_stand"),
            new LastStandEffect(StatusEffectCategory.NEUTRAL, 8401224))
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "054cac4c-3bcd-449e-b5d0-c4ae6c21b3c9",
                    -0.95D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "bc3726c1-95d2-40f3-b775-091b2646d8e2",
                    0.35D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "908ec8f3-f5fe-48e9-8b7f-8c1aea9e63ea",
                    0.35D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect DISCONTENT = Registry.register(Registry.STATUS_EFFECT,
            new Identifier(ThatcherMod.MOD_ID, "discontent"),
            new DiscontentEffect(StatusEffectCategory.HARMFUL, 11260403))
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "7b707fcf-083d-48d7-9d8e-36bc289a8cab",
                    -0.125D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);


    public static void registerModEffects(){
        ThatcherMod.LOGGER.debug("Registering Effects for " + ThatcherMod.MOD_ID + " ...");
    }
}
