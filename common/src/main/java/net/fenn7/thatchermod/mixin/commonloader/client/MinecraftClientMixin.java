package net.fenn7.thatchermod.mixin.commonloader.client;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.network.ModPackets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"), cancellable = true)
    private void injectedAttackMethod(CallbackInfoReturnable<Boolean> cir) {
        boolean shouldCancel = false;
        if (player.getMainHandStack().isOf(ModItems.COMMAND_SCEPTRE.get())) {
            NetworkManager.sendToServer(ModPackets.CS_C2S_ID, new PacketByteBuf(Unpooled.buffer()));
            shouldCancel = true;
        }
        if (player.getMainHandStack().isOf(ModItems.GRENADE_LAUNCHER.get())) {
            NetworkManager.sendToServer(ModPackets.GL_C2S_ID, new PacketByteBuf(Unpooled.buffer()));
            shouldCancel = true;
        }
        if (shouldCancel) {
            cir.cancel();
        }
    }
}
