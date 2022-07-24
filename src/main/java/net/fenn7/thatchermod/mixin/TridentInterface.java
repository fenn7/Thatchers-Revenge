package net.fenn7.thatchermod.mixin;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TridentEntity.class)
public interface TridentInterface {

    @Invoker("asItemStack")
    public ItemStack asItemStack();
}
