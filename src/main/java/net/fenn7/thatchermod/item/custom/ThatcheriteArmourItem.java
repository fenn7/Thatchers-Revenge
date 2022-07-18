package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ThatcheriteArmourItem extends ArmorItem {
    private boolean hasUpdatedStatus;

    public ThatcheriteArmourItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
        this.hasUpdatedStatus = false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            if (hasHelmet(player)) {
                if (!this.hasUpdatedStatus) {
                    for (StatusEffectInstance effect : player.getStatusEffects()) {
                        if (effect.getEffectType().isBeneficial()) {
                            player.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), (int) (1.5 * effect.getDuration()),
                                    effect.getAmplifier() + 1));
                        }
                    }
                    this.hasUpdatedStatus = true;
                }
            }
            else {
                this.hasUpdatedStatus = false;
            }
            if (hasChest(player)) {
                // reduces explosion damage by 50% if equipped (this is handled in PlayerEntityMixin)
            }
            if (hasBoots(player)) {

            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private boolean hasHelmet(PlayerEntity player) {
        ItemStack helmet = player.getInventory().getArmorStack(3);
        return !helmet.isEmpty() && helmet.getItem().equals(ModItems.THATCHERITE_HELMET);
    }

    private boolean hasChest(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && chest.getItem().equals(ModItems.THATCHERITE_CHESTPLATE);
    }
    private boolean hasLegs(PlayerEntity player) {
        ItemStack legs = player.getInventory().getArmorStack(1);
        return !legs.isEmpty() && legs.getItem().equals(ModItems.THATCHERITE_GREAVES);
    }
    private boolean hasBoots(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        return !boots.isEmpty() && boots.getItem().equals(ModItems.THATCHERITE_BOOTS);
    }
}
