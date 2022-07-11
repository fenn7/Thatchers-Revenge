package net.fenn7.thatchermod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fenn7.thatchermod.block.ModBlocks;
import net.fenn7.thatchermod.block.entity.ModBlockEntities;
import net.fenn7.thatchermod.callback.ModCallbacks;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.item.custom.ThatcherSoulItem;
import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.screen.ModScreenHandlers;
import net.fenn7.thatchermod.screen.ThatcherismAltarScreen;
import net.fenn7.thatchermod.util.ModRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

import java.rmi.registry.Registry;

public class ThatcherMod implements ModInitializer {
	public static final String MOD_ID = "thatchermod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		GeckoLib.initialize();
		LOGGER.info("subscribe to bruno powroznik");

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEffects.registerModEffects();

		ModParticles.registerParticles();

		ModBlockEntities.registerModBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

		ModRegistries.registerAll();
		ModCallbacks.registerAllEvents();
		ModCommands.registerAllCommands();
		ModEvents.registerAllEvents();
	}
}
