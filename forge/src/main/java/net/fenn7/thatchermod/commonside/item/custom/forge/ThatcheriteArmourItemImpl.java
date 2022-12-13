package net.fenn7.thatchermod.commonside.item.custom.forge;

import net.fenn7.thatchermod.commonside.item.custom.ThatcheriteArmourItem;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ThatcheriteArmourItemImpl extends ThatcheriteArmourItem {

    private static IAnimatableModel<Object> model;

    public ThatcheriteArmourItemImpl(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    public static Item create(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        return new ThatcheriteArmourItemImpl(material, slot, settings);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Override
            public BipedEntityModel<?> getArmorModel(
                    LivingEntity entityLiving,
                    ItemStack itemStack,
                    EquipmentSlot armorSlot,
                    BipedEntityModel<?> defaultModel
            ) {
                return (BipedEntityModel<?>) GeoArmorRenderer.getRenderer(ThatcheriteArmourItem.class, entityLiving)
                        .applyEntityStats(defaultModel)
                        .setCurrentItem(entityLiving, itemStack, armorSlot)
                        .applySlot(armorSlot);
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nullable
    @Override
    public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        GeoArmorRenderer renderer = GeoArmorRenderer.getRenderer(ThatcheriteArmourItem.class, entity);
        return renderer.getTextureLocation((ArmorItem) stack.getItem()).toString();
    }

    @SuppressWarnings("unchecked")
    public static void setModel(IAnimatableModel<?> model) {
        ThatcheriteArmourItemImpl.model = (IAnimatableModel<Object>) model;
    }

    static {
        AnimationController.addModelFetcher(animatable -> {
            if (animatable instanceof ThatcheriteArmourItem) {
                return model;
            } else {
                return null;
            }
        });
    }
}
