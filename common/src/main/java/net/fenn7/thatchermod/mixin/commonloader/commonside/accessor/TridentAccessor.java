package net.fenn7.thatchermod.mixin.commonloader.commonside.accessor;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TridentEntity.class)
public interface TridentAccessor {

    @Invoker("asItemStack")
    ItemStack thatchersRevenge$callAsItemStack();

    @Accessor("tridentStack")
    ItemStack thatchersRevenge$getTridentStack();

    @Accessor("tridentStack")
    void thatchersRevenge$setTridentStack(ItemStack stack);

    @Accessor("dealtDamage")
    boolean thatchersRevenge$hasDealtDamage();

    @Accessor("dealtDamage")
    void thatchersRevenge$setDealtDamage(boolean dealt);
}
