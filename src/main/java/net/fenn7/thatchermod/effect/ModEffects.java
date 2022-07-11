package net.fenn7.thatchermod.effect;

import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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

    public static void registerModEffects(){
        ThatcherMod.LOGGER.debug("Registering Effects for " + ThatcherMod.MOD_ID + " ...");
    }
}
