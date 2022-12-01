package net.fenn7.thatchermod.commonside.item.custom.grenade;

import net.fenn7.thatchermod.commonside.entity.projectiles.AbstractGrenadeEntity;
import net.fenn7.thatchermod.commonside.entity.projectiles.SmokeGrenadeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class SmokeGrenadeItem extends AbstractGrenadeItem {
    public SmokeGrenadeItem(Settings settings) {
        super(settings);
        this.defaultSpeed = 0.8F;
    }

    @Override
    protected AbstractGrenadeEntity createGrenadeAt(World world, PlayerEntity player) {
        return new SmokeGrenadeEntity(world, player);
    }
}
