package net.fenn7.thatchermod.commonside.forge;

import dev.architectury.platform.forge.EventBuses;
import net.fenn7.thatchermod.client.ThatcherModClient;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(ThatcherMod.MOD_ID)
public class ThatcherModForge {

    public ThatcherModForge() {
        init();
        if (FMLLoader.getDist().isClient()) {
            clientInit();
        }
    }

    private static void init() {
        EventBuses.registerModEventBus(ThatcherMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ThatcherMod.init();
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientInit() {
        ThatcherMod.LOGGER.warn("FORGE IT UP");
        ThatcherModClient.init();
    }
}
