package net.fenn7.thatchermod.item.custom.grenade;

import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.FireGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireGrenadeItem extends AbstractGrenadeItem {
    public FireGrenadeItem(Settings settings) {
        super(settings);
        this.defaultSpeed = 0.675F;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    public AbstractGrenadeEntity createGrenadeAt(World world, PlayerEntity player) {
        return new FireGrenadeEntity(world, player);
    }
}
