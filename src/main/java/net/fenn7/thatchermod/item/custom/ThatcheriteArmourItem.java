package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThatcheriteArmourItem extends ArmorItem implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public ThatcheriteArmourItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static boolean hasFullSet(PlayerEntity player) {
        return hasHelmet(player) && hasChest(player) && hasLegs(player) && hasBoots(player);
    }

    public static boolean hasHelmet(PlayerEntity player) {
        ItemStack helmet = player.getInventory().getArmorStack(3);
        return !helmet.isEmpty() && helmet.getItem().equals(ModItems.THATCHERITE_HELMET);
    }

    public static boolean hasChest(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && chest.getItem().equals(ModItems.THATCHERITE_CHESTPLATE);
    }
    public static boolean hasLegs(PlayerEntity player) {
        ItemStack legs = player.getInventory().getArmorStack(1);
        return !legs.isEmpty() && legs.getItem().equals(ModItems.THATCHERITE_GREAVES);
    }
    public static boolean hasBoots(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        return !boots.isEmpty() && boots.getItem().equals(ModItems.THATCHERITE_BOOTS);
    }

    /* animation stuff */
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        // This is all the extradata this event carries. The livingentity is the entity
        // that's wearing the armor. The itemstack and equipmentslottype are self
        // explanatory.
        LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);

        // Always loop the animation but later on in this method we'll decide whether or
        // not to actually play it
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));

        // If the living entity is an armorstand just play the animation nonstop
        if (livingEntity instanceof ArmorStandEntity) {
            return PlayState.CONTINUE;
        }

        // The entity is a player, so we want to only play if the player is wearing the
        // full set of armor
        else if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;

            // Get all the equipment, aka the armor, currently held item, and offhand item
            List<Item> equipmentList = new ArrayList<>();
            player.getItemsEquipped().forEach((x) -> equipmentList.add(x.getItem()));

            // elements 2 to 6 are the armor so we take the sublist. Armorlist now only
            // contains the 4 armor slots
            List<Item> armorList = equipmentList.subList(2, 6);

            // Make sure the player is wearing all the armor. If they are, continue playing
            // the animation, otherwise stop
            boolean isWearingAll = armorList.containsAll(Arrays.asList(ModItems.THATCHERITE_BOOTS,
                    ModItems.THATCHERITE_GREAVES, ModItems.THATCHERITE_CHESTPLATE, ModItems.THATCHERITE_HELMET));
            return isWearingAll ? PlayState.CONTINUE : PlayState.STOP;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
