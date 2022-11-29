package net.fenn7.thatchermod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.entity.mobs.RoyalFencerEntity;
import net.fenn7.thatchermod.entity.mobs.RoyalGrenadierEntity;
import net.fenn7.thatchermod.entity.mobs.ThatcherEntity;
import net.fenn7.thatchermod.entity.projectiles.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<ThatcherEntity> THATCHER = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "thatcher"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ThatcherEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f, 1.9f)).build());

    public static final EntityType<RoyalFencerEntity> ROYAL_FENCER = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "royal_fencer"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RoyalFencerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.7f, 1.8f)).build());

    public static final EntityType<RoyalGrenadierEntity> ROYAL_GRENADIER = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "royal_grenadier"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RoyalGrenadierEntity::new)
                    .dimensions(EntityDimensions.fixed(0.7f, 1.8f)).build());

    public static final EntityType<CursedMeteorEntity> CURSED_METEOR = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "cursed_meteor"),
            FabricEntityTypeBuilder.<CursedMeteorEntity>create(SpawnGroup.MISC, CursedMeteorEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75F, 0.75F))
                    .trackRangeBlocks(64).trackedUpdateRate(10).build());

    public static final EntityType<CursedMissileEntity> CURSED_MISSILE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "cursed_missile"),
            FabricEntityTypeBuilder.<CursedMissileEntity>create(SpawnGroup.MISC, CursedMissileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375F, 0.375F))
                    .trackRangeBlocks(32).trackedUpdateRate(10).build());

    public static final EntityType<SmokeEntity> SMOKE_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "smoke_entity"),
            FabricEntityTypeBuilder.<SmokeEntity>create(SpawnGroup.MISC, SmokeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.001F, 0.001F))
                    .trackRangeBlocks(16).trackedUpdateRate(10).build());

    public static final EntityType<GrenadeEntity> GRENADE_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "grenade_entity"),
            FabricEntityTypeBuilder.<GrenadeEntity>create(SpawnGroup.MISC, GrenadeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.375F))
                    .trackRangeBlocks(48).trackedUpdateRate(10).build());

    public static final EntityType<FireGrenadeEntity> FIRE_GRENADE_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "fire_grenade_entity"),
            FabricEntityTypeBuilder.<FireGrenadeEntity>create(SpawnGroup.MISC, FireGrenadeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.375F))
                    .trackRangeBlocks(48).trackedUpdateRate(10).build());

    public static final EntityType<SmokeGrenadeEntity> SMOKE_GRENADE_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "smoke_grenade_entity"),
            FabricEntityTypeBuilder.<SmokeGrenadeEntity>create(SpawnGroup.MISC, SmokeGrenadeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.375F))
                    .trackRangeBlocks(48).trackedUpdateRate(10).build());

    public static final EntityType<TrickleDownTridentEntity> TRICKLE_DOWN_TRIDENT_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "trickle_down_trident_entity"),
            FabricEntityTypeBuilder.<TrickleDownTridentEntity>create(SpawnGroup.MISC, TrickleDownTridentEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(96).trackedUpdateRate(10).build());
}