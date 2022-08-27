package net.fenn7.thatchermod.item.custom.grenade;

import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.GrenadeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GrenadeItem extends Item {
    public GrenadeItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
		user.getItemCooldownManager().set(this, 10);

        if (!world.isClient) {
            GrenadeEntity grenade = new GrenadeEntity(world, user);
            grenade.setItem(itemStack);
            grenade.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.0F, 0.2F);
            world.spawnEntity(grenade);
        }

        if (!user.isCreative()) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
