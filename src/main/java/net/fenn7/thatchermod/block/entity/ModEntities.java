package net.fenn7.thatchermod.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fenn7.thatchermod.ThatcherMod;
import net.fenn7.thatchermod.block.entity.custom.CursedMeteorEntity;
import net.fenn7.thatchermod.block.entity.custom.CursedMissileEntity;
import net.fenn7.thatchermod.block.entity.custom.ThatcherEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<ThatcherEntity> THATCHER = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "thatcher"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ThatcherEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 2.0f)).build());

    public static final EntityType<CursedMeteorEntity> CURSED_METEOR = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "cursed_meteor"),
            FabricEntityTypeBuilder.<CursedMeteorEntity>create(SpawnGroup.MISC, CursedMeteorEntity::new)
					.dimensions(EntityDimensions.fixed(0.75F, 0.75F))
                    .trackRangeBlocks(64).trackedUpdateRate(10).build());

    public static final EntityType<CursedMissileEntity> CURSED_MISSILE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(ThatcherMod.MOD_ID, "cursed_missile"),
            FabricEntityTypeBuilder.<CursedMissileEntity>create(SpawnGroup.MISC, CursedMissileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(32).trackedUpdateRate(10).build());
}