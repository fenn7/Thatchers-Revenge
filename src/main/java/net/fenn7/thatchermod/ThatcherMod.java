package net.fenn7.thatchermod;

import net.fabricmc.api.ModInitializer;
import net.fenn7.thatchermod.block.ModBlocks;
import net.fenn7.thatchermod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThatcherMod implements ModInitializer {
	public static final String MOD_ID = "thatchermod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("subscribe to bruno powroznik");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}
