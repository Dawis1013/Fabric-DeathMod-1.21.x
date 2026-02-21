package net.dawis.deathmod.block.entity;

import net.dawis.deathmod.DeathMod;
import net.dawis.deathmod.block.ModBlocks;
import net.dawis.deathmod.block.entity.custom.GraveBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<GraveBlockEntity> GRAVE_BLOCK_ENTITY =
            register("grave_block_entity", FabricBlockEntityTypeBuilder.create(GraveBlockEntity::new, ModBlocks.GRAVE).build());

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(DeathMod.MOD_ID, path), blockEntityType);
    }

    public static void init() {
        DeathMod.LOGGER.info("Initializing Modded Blocks' Entities");
    }
}
