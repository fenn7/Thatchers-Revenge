package net.fenn7.thatchermod.commonside.forge;

import dev.architectury.platform.forge.EventBuses;
import net.fenn7.thatchermod.client.ThatcherModClient;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

    private void setup(final FMLCommonSetupEvent event) {
        ThatcherMod.LOGGER.warn("setting");
        event.enqueueWork(() -> {
            SpawnRestriction.register(ModEntities.ROYAL_GRENADIER.get(),
                    SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    MobEntity::canMobSpawn);

            SpawnRestriction.register(ModEntities.ROYAL_FENCER.get(),
                    SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    MobEntity::canMobSpawn);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientInit() {
        ThatcherMod.LOGGER.warn("FORGE IT UP");
        ThatcherModClient.init();
    }
}
