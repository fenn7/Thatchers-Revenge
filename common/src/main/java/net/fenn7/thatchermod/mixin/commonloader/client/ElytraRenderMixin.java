package net.fenn7.thatchermod.mixin.commonloader.client;

import net.fenn7.thatchermod.commonside.enchantments.ModEnchantments;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraRenderMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
                        T entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        int sLevel = EnchantmentHelper.getLevel(ModEnchantments.STEALTH.get(), entity.getEquippedStack(EquipmentSlot.CHEST));
        if (entity instanceof PlayerEntity player && player.isFallFlying() && sLevel != 0)
            ci.cancel();
    }
}
