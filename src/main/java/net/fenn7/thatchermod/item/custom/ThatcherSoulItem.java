package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.block.ModBlocks;
import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.particle.custom.ThatcherJumpParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.EventListener;

public class ThatcherSoulItem extends Item{
    public ThatcherSoulItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        if (user instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity)user);
                    player.sendMessage(Text.literal("§4AERRRAUUUUGHAAAAAAAARRRRRRRRRRAGHHHHHHHHAAAAAAAAA!!!!!!!"), true);
                    player.setOnFireFor(3);
                    player.playSound(new SoundEvent(new Identifier("minecraft:block.glass.break")), 4, 1f);
                    player.playSound(new SoundEvent(new Identifier("thatchermod:thatcher_possession")), 4, 0.75f);
                    world.addParticle(ModParticles.THATCHER_JUMPSCARE, player.getX(), player.getY(), player.getZ(),1, 1, 1);
        }
        return itemStack;
    }
}
