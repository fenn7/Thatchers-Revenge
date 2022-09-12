package net.fenn7.thatchermod.item.custom.grenade;

import net.fenn7.thatchermod.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.entity.projectiles.SmokeGrenadeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class SmokeGrenadeItem extends AbstractGrenadeItem {
    public SmokeGrenadeItem(Settings settings) {
        super(settings);
        this.defaultSpeed = 1.0F;
    }

    @Override
    protected AbstractGrenadeEntity createGrenadeAt(World world, PlayerEntity player) {
        return new SmokeGrenadeEntity(world, player);
    }
}
