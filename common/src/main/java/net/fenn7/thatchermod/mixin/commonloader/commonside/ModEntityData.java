package net.fenn7.thatchermod.mixin.commonloader.commonside;

import net.fenn7.thatchermod.commonside.util.ThatcherModEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityData implements ThatcherModEntityData {
    private NbtCompound nbtC;

    @Override
    public NbtCompound thatchersRevenge$getPersistentData() {
        if (this.nbtC == null) {
            this.nbtC = new NbtCompound();
        }
        return nbtC;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void thatchersRevenge$injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (nbtC != null) {
            nbt.put("ModData", nbtC);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void thatchersRevenge$injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("ModData", 10)) {
            nbtC = nbt.getCompound("ModData");
        }
    }
}



