package net.fenn7.thatchermod.commonside.world.gen;

import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import java.util.Arrays;
import java.util.List;

public class ThatcherModEntityGeneration {
    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        addEntityToAllOverworldBiomes(event, ModEntities.ROYAL_GRENADIER.get(),
                500, 2, 4);
        addEntityToAllOverworldBiomes(event, ModEntities.ROYAL_FENCER.get(),
                500, 2, 3);
    }

    private static void addEntityToAllBiomesExceptThese(BiomeLoadingEvent event, EntityType<?> type,
                                                        int weight, int minCount, int maxCount, RegistryKey<Biome>... biomes) {
        // Goes through each entry in the biomes and sees if it matches the current biome we are loading
        boolean isBiomeSelected = Arrays.stream(biomes).map(RegistryKey::getValue)
                .map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if(!isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    @SafeVarargs
    private static void addEntityToSpecificBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount, RegistryKey<Biome>... biomes) {
        // Goes through each entry in the biomes and sees if it matches the current biome we are loading
        boolean isBiomeSelected = Arrays.stream(biomes).map(RegistryKey::getValue)
                .map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if(isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllOverworldBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                      int weight, int minCount, int maxCount) {
        if(!event.getCategory().equals(Biome.Category.THEEND) && !event.getCategory().equals(Biome.Category.NETHER)) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllBiomesNoNether(BiomeLoadingEvent event, EntityType<?> type,
                                                     int weight, int minCount, int maxCount) {
        if(!event.getCategory().equals(Biome.Category.NETHER)) {
            List<SpawnSettings.SpawnEntry> base = event.getSpawns().getSpawner(type.getSpawnGroup());
            base.add(new SpawnSettings.SpawnEntry(type,weight, minCount, maxCount));
        }
    }

    private static void addEntityToAllBiomesNoEnd(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount) {
        if(!event.getCategory().equals(Biome.Category.NETHER)) {
            List<SpawnSettings.SpawnEntry> base = event.getSpawns().getSpawner(type.getSpawnGroup());
            base.add(new SpawnSettings.SpawnEntry(type,weight, minCount, maxCount));
        }
    }

    private static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        List<SpawnSettings.SpawnEntry> base = event.getSpawns().getSpawner(type.getSpawnGroup());
        base.add(new SpawnSettings.SpawnEntry(type,weight, minCount, maxCount));
    }
}