package net.fenn7.thatchermod.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    public static final ItemGroup THATCHER = FabricItemGroupBuilder.build(new Identifier(ThatcherMod.MOD_ID,
            "thatcher"), () -> new ItemStack(ModBlocks.THATCHERISM_ALTAR));

}
