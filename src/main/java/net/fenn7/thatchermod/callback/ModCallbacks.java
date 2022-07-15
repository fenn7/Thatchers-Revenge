package net.fenn7.thatchermod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fenn7.thatchermod.block.entity.custom.CursedMissileEntity;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

import java.awt.event.ActionEvent;
import java.util.List;

public class ModCallbacks implements CommonMethods {

    public static void registerAllEvents() {
        registerScepterEvent();
    }

    private static void registerScepterEvent() {
    }

    private static void summonDustParticles(World world, int number, float red, float green, float blue, float f,
                                     double x, double y, double z, double velX, double velY, double velZ) {
        DustParticleEffect dust = new DustParticleEffect(new Vec3f(red, green, blue), f);
        for (int i = 0; i < number; i++) {
            world.addParticle(dust, x, y, z, velX, velY, velZ);
        }
    }
}

