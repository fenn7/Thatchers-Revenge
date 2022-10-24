package net.fenn7.thatchermod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {

    @Redirect(method = "onStoppedUsing", at = @At(value = "NEW", target = "net/minecraft/entity/projectile/TridentEntity"))
    private TridentEntity bruh(World world, LivingEntity owner, ItemStack stack) {

        return new TridentEntity(world, owner, stack);
    }
}
