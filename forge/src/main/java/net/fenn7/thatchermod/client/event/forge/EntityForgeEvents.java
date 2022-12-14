package net.fenn7.thatchermod.client.event.forge;

import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.world.gen.ThatcherModEntityGeneration;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ThatcherMod.MOD_ID)
public class EntityForgeEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ThatcherModEntityGeneration.onEntitySpawn(event);
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
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
}
