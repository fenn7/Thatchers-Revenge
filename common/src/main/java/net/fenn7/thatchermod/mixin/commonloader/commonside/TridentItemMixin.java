package net.fenn7.thatchermod.mixin.commonloader.commonside;

import com.google.common.collect.Multimap;
import net.fenn7.thatchermod.commonside.entity.projectiles.TrickleDownTridentEntity;
import net.fenn7.thatchermod.commonside.item.custom.TrickleDownTridentItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin extends Item {

    @Shadow
    @Final
    private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public TridentItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "onStoppedUsing", at = @At(value = "STORE"), ordinal = 0)
    private TridentEntity redirectToCustom(TridentEntity entity, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (this.asItem() instanceof TrickleDownTridentItem) {
            entity = new TrickleDownTridentEntity(world, user, stack);
        }
        return entity;
    }
}
