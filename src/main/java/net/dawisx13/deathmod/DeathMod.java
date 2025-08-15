package net.dawisx13.deathmod;

import net.dawisx13.deathmod.block.ModBlocks;
import net.dawisx13.deathmod.block.entity.ModBlockEntities;
import net.dawisx13.deathmod.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;

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

        ModBlocks.init();
        ModBlockEntities.init();
        ModScreenHandlers.init();
	}
}