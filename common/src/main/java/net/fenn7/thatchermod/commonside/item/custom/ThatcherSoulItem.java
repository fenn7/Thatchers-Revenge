package net.fenn7.thatchermod.commonside.item.custom;

import net.fenn7.thatchermod.commonside.particle.ModParticles;
import net.fenn7.thatchermod.commonside.util.CommonMethods;
import net.fenn7.thatchermod.commonside.util.ModText;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
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
        if (user instanceof PlayerEntity player) {
            player.sendMessage(ModText.literal("§4AERRRAUUUUGHAAAAAAAARRRRRRRRRRAGHHHHHHHHAAAAAAAAA!!!!!!!"), true);
            player.setOnFireFor(3);
            player.playSound(new SoundEvent(new Identifier("minecraft:block.glass.break")), 4, 1f);
            player.playSound(new SoundEvent(new Identifier("thatchermod:thatcher_possession")), 4, 0.75f);

            Vec3d inFrontOfFaceVec = player.getEyePos().add(player.getRotationVector().normalize().multiply(0.5));
            for (int i = 0; i < 25; i++) {
                world.addParticle(ModParticles.THATCHER_JUMPSCARE.get(), inFrontOfFaceVec.getX(), inFrontOfFaceVec.getY(),
                        inFrontOfFaceVec.getZ(), 0, 0, 0);
            }
        }
        return itemStack;
    }

    /*@Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var eye = user.getEyePos();
        var range = eye.add(user.getRotationVec(1.0F).normalize().multiply(10));
        user.sendMessage(Text.of(CommonMethods.getAllEntityCollisions(world, user, eye, range
                , new Box(eye, range),
                x -> true, 0).toString()), false);
        CommonMethods.spawnParticleCircle(world, ParticleTypes.CAMPFIRE_COSY_SMOKE, user.getX(), user.getY(), user.getZ(), 2);
        return super.use(world, user, hand);
    }*/
}
