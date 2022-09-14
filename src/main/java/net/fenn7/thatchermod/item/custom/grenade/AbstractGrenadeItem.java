package net.fenn7.thatchermod.item.custom.grenade;

import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public abstract class AbstractGrenadeItem extends Item {
    protected float defaultRoll = 0.1F;
    protected float defaultSpeed = 0.7F;
    protected float defaultDiv = 0.2F;

    public AbstractGrenadeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 20);

        if (!world.isClient) {
            AbstractGrenadeEntity grenade = createGrenadeAt(world, user);
            grenade.setItem(itemStack);
            setPitchYawVelocity(user, grenade, this.defaultRoll, this.defaultSpeed, this.defaultDiv);
            world.spawnEntity(grenade);
        }

        if (!user.isCreative()) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    protected void setPitchYawVelocity(PlayerEntity user, AbstractGrenadeEntity grenade, float roll, float speed, float divergence) {
        grenade.setVelocity(user, user.getPitch(), user.getYaw(), roll, speed, divergence);
    }

    protected abstract AbstractGrenadeEntity createGrenadeAt(World world, PlayerEntity player);
}
