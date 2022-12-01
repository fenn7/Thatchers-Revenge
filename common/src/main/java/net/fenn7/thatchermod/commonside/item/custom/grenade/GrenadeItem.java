package net.fenn7.thatchermod.commonside.item.custom.grenade;

import net.fenn7.thatchermod.commonside.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.commonside.entity.projectiles.GrenadeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GrenadeItem extends AbstractGrenadeItem {
    public GrenadeItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    public AbstractGrenadeEntity createGrenadeAt(World world, PlayerEntity player) {
        return new GrenadeEntity(world, player);
    }
}
