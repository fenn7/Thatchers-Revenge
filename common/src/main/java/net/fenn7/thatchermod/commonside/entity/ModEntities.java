package net.fenn7.thatchermod.commonside.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.commonside.ThatcherMod;
import net.fenn7.thatchermod.commonside.entity.mobs.*;
import net.fenn7.thatchermod.commonside.entity.projectiles.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ModEntities {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.ENTITY_TYPE_KEY);

    public static final RegistrySupplier<EntityType<ThatcherEntity>> THATCHER = ENTITY_TYPES.register(
            "thatcher",
            () -> EntityType.Builder.create(ThatcherEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.9F, 1.9F)
                    .build("thatcher")
    );

    public static final RegistrySupplier<EntityType<RoyalFencerEntity>> ROYAL_FENCER = ENTITY_TYPES.register(
            "royal_fencer",
            () -> EntityType.Builder.create(RoyalFencerEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(0.7F, 1.8F)
                    .build("royal_fencer")
    );

    public static final RegistrySupplier<EntityType<RoyalGrenadierEntity>> ROYAL_GRENADIER = ENTITY_TYPES.register(
            "royal_grenadier",
            () -> EntityType.Builder.create(RoyalGrenadierEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(0.7F, 1.8F)
                    .build("royal_grenadier")
    );

    public static final RegistrySupplier<EntityType<CursedMeteorEntity>> CURSED_METEOR = ENTITY_TYPES.register(
            "cursed_meteor",
            () -> EntityType.Builder.<CursedMeteorEntity>create(CursedMeteorEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.75F, 0.75F)
                    .maxTrackingRange(blocksToChunks(64))
                    .trackingTickInterval(10)
                    .build("cursed_meteor")
    );

    public static final RegistrySupplier<EntityType<CursedMissileEntity>> CURSED_MISSILE = ENTITY_TYPES.register(
            "cursed_missile",
            () -> EntityType.Builder.<CursedMissileEntity>create(CursedMissileEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.375F, 0.375F)
                    .maxTrackingRange(blocksToChunks(32))
                    .trackingTickInterval(10)
                    .build("cursed_missile")
    );

    public static final RegistrySupplier<EntityType<SmokeEntity>> SMOKE_ENTITY = ENTITY_TYPES.register(
            "smoke_entity",
            () -> EntityType.Builder.<SmokeEntity>create(SmokeEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.001F, 0.001F)
                    .maxTrackingRange(blocksToChunks(16))
                    .trackingTickInterval(10)
                    .build("smoke_entity")
    );

    public static final RegistrySupplier<EntityType<RedMagicIndicatorEntity>> RED_MAGIC_ENTITY = ENTITY_TYPES.register(
            "red_magic_entity",
            () -> EntityType.Builder.<RedMagicIndicatorEntity>create(RedMagicIndicatorEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.001F, 0.001F)
                    .maxTrackingRange(blocksToChunks(16))
                    .trackingTickInterval(10)
                    .build("red_magic_entity")
    );

    public static final RegistrySupplier<EntityType<GrenadeEntity>> GRENADE_ENTITY = ENTITY_TYPES.register(
            "grenade_entity",
            () -> EntityType.Builder.<GrenadeEntity>create(GrenadeEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.25F, 0.375F)
                    .maxTrackingRange(blocksToChunks(48))
                    .trackingTickInterval(10)
                    .build("grenade_entity")
    );

    public static final RegistrySupplier<EntityType<FireGrenadeEntity>> FIRE_GRENADE_ENTITY = ENTITY_TYPES.register(
            "fire_grenade_entity",
            () -> EntityType.Builder.<FireGrenadeEntity>create(FireGrenadeEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.25F, 0.375F)
                    .maxTrackingRange(blocksToChunks(48))
                    .trackingTickInterval(10)
                    .build("fire_grenade_entity")
    );

    public static final RegistrySupplier<EntityType<SmokeGrenadeEntity>> SMOKE_GRENADE_ENTITY = ENTITY_TYPES.register(
            "smoke_grenade_entity",
            () -> EntityType.Builder.<SmokeGrenadeEntity>create(SmokeGrenadeEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.25F, 0.375F)
                    .maxTrackingRange(blocksToChunks(48))
                    .trackingTickInterval(10)
                    .build("smoke_grenade_entity")
    );

    public static final RegistrySupplier<EntityType<TrickleDownTridentEntity>> TRICKLE_DOWN_TRIDENT_ENTITY = ENTITY_TYPES.register(
            "trickle_down_trident_entity",
            () -> EntityType.Builder.<TrickleDownTridentEntity>create(TrickleDownTridentEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.5F, 0.5F)
                    .maxTrackingRange(blocksToChunks(96))
                    .trackingTickInterval(10)
                    .build("trickle_down_trident_entity")
    );

    public static final RegistrySupplier<EntityType<FirstSpectreEntity>> FIRST_SPECTRE_ENTITY = ENTITY_TYPES.register(
            "first_spectre_entity",
            () -> EntityType.Builder.<FirstSpectreEntity>create(FirstSpectreEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.85F, 3.8F)
                    .maxTrackingRange(blocksToChunks(96))
                    .trackingTickInterval(10)
                    .build("first_spectre_entity")
    );

    public static final RegistrySupplier<EntityType<SecondSpectreEntity>> SECOND_SPECTRE_ENTITY = ENTITY_TYPES.register(
            "second_spectre_entity",
            () -> EntityType.Builder.<SecondSpectreEntity>create(SecondSpectreEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(1.7F, 2.8F)
                    .maxTrackingRange(blocksToChunks(96))
                    .trackingTickInterval(10)
                    .build("second_spectre_entity")
    );

    public static final RegistrySupplier<EntityType<ThirdSpectreEntity>> THIRD_SPECTRE_ENTITY = ENTITY_TYPES.register(
            "third_spectre_entity",
            () -> EntityType.Builder.<ThirdSpectreEntity>create(ThirdSpectreEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.95F, 2.0F)
                    .maxTrackingRange(blocksToChunks(96))
                    .trackingTickInterval(10)
                    .build("third_spectre_entity")
    );

    public static void registerModEntities() {
        ENTITY_TYPES.register();
    }

    private static int blocksToChunks(int blocks) {
        return (blocks + 15) / 16;
    }
}