package net.fenn7.thatchermod.item.custom;

import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.util.ModText;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ThatcherSoulItem extends Item {
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
            PlayerEntity player = ((PlayerEntity) user);
            player.sendMessage(ModText.literal("§4AERRRAUUUUGHAAAAAAAARRRRRRRRRRAGHHHHHHHHAAAAAAAAA!!!!!!!"), true);
            player.setOnFireFor(3);
            player.playSound(new SoundEvent(new Identifier("minecraft:block.glass.break")), 4, 1f);
            player.playSound(new SoundEvent(new Identifier("thatchermod:thatcher_possession")), 4, 0.75f);

            for (int i = 0; i < 25; i++) {
                world.addParticle(ModParticles.THATCHER_JUMPSCARE, player.getX(), player.getY() + 1.4f, player.getZ(), 0, 0, 0);
            }
        }
        return itemStack;
    }
}
