package net.fenn7.thatchermod.callback;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.UnionBusterItem;
import net.fenn7.thatchermod.util.CommonMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
        registerUnionBusterEvent();
        registerMilkSnatcherEvent();
    }

    private static void registerUnionBusterEvent() {
    }

    private static void registerMilkSnatcherEvent() {
        /*UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!player.isSpectator() && player.getMainHandStack().getItem() == (ModItems.MILK_SNATCHER)
                    && !player.getItemCooldownManager().isCoolingDown(ModItems.MILK_SNATCHER)) {

                List<Entity> nearbyEntities = CommonMethods.getEntitiesNearPlayer(player, -4, 0, -4, 4, 4, 4, world);

                if (nearbyEntities.size() > 1) {
                    summonDustParticles(world, 3, 1.0f, 1.0f, 1.0f, 3,
                            player.getX(), player.getY() + 2, player.getZ(), 0, 0, 0);
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_STRAY_DEATH,
                            SoundCategory.BLOCKS, 15F, 0.33F);
                }

                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity && entity != player) {
                        summonDustParticles(world, 1, 1.0f, 1.0f, 1.0f, 2,
                                entity.getX(), entity.getY() + 0.5D, entity.getZ(), 0, 0, 0);
                    }
                }
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });*/
    }

    private static void summonDustParticles(World world, int number, float red, float green, float blue, float f,
                                     double x, double y, double z, double velX, double velY, double velZ) {
        DustParticleEffect dust = new DustParticleEffect(new Vec3f(red, green, blue), f);
        for (int i = 0; i < number; i++) {
            world.addParticle(dust, x, y, z, velX, velY, velZ);
        }
    }
}

