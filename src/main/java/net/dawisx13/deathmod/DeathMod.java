package net.dawisx13.deathmod;

import net.dawisx13.deathmod.block.ModBlocks;
import net.dawisx13.deathmod.block.entity.ModBlockEntities;
import net.dawisx13.deathmod.block.entity.custom.GraveBlockEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathMod implements ModInitializer {
	public static final String MOD_ID = "deathmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing Death mod");

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            ServerWorld world = oldPlayer.getWorld();
            BlockPos deathPos = oldPlayer.getBlockPos();
            BlockPos gravePos = findSuitableGravePos(world, deathPos);

            // Sets the Grave on the death position (or near it) of the player
            world.setBlockState(gravePos, ModBlocks.GRAVE.getDefaultState());

            // Transfer player's inventory to the grave
            BlockEntity blockEntity = world.getBlockEntity(gravePos);
            if (blockEntity instanceof GraveBlockEntity graveBlockEntity) {
                DefaultedList<ItemStack> graveInv = DefaultedList.of();
                graveInv.addAll(oldPlayer.getInventory().getMainStacks());
                graveInv.add(oldPlayer.getInventory().getStack(36)); //TODO get armor and offhand stacks?

                graveBlockEntity.setInventory(graveInv);
            }
        });

        ModBlocks.init();
        ModBlockEntities.init();
	}

    private BlockPos findSuitableGravePos(World world, BlockPos pos) {
        while (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down()) && pos.getY() > world.getBottomY()) {
            pos = pos.down();
        }

        return pos;
    }
}