package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TridentEntity.class)
public interface TridentInterface {
    @Invoker("asItemStack")
    ItemStack callAsItemStack();

    @Accessor("tridentStack")
    ItemStack getTridentStack();

    @Accessor("tridentStack")
    void setTridentStack(ItemStack stack);

    @Accessor("dealtDamage")
    boolean hasDealtDamage();

    @Accessor("dealtDamage")
    void setDealtDamage(boolean dealt);
}
