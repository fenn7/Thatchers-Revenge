package net.fenn7.thatchermod.mixin;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> {
    @Inject(method = "setAngles", at = @At("TAIL"))
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
    }
}
