package net.fenn7.thatchermod.commonside.item;

import dev.architectury.registry.CreativeTabRegistry;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    public static final ItemGroup THATCHER = CreativeTabRegistry.create(
            new Identifier(ThatcherMod.MOD_ID, "thatcher"),
            () -> new ItemStack(ModItems.THATCHERISM_ALTAR.get())
    );
}
