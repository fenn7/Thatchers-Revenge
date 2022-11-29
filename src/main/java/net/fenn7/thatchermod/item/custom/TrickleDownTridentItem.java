package net.fenn7.thatchermod.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.TridentItem;

public class TrickleDownTridentItem extends TridentItem {
    private final ImmutableMultimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public TrickleDownTridentItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 10.0D, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -2.70D, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
        ThatcherMod.LOGGER.warn(attributeModifiers.toString());
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return this.attributeModifiers;
    }
}
