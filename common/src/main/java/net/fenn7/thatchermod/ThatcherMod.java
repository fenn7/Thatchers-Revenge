package net.fenn7.thatchermod;

import net.fenn7.thatchermod.block.ModBlockEntities;
import net.fenn7.thatchermod.block.ModBlocks;
import net.fenn7.thatchermod.callback.ModCallbacks;
import net.fenn7.thatchermod.effect.ModEffects;
import net.fenn7.thatchermod.enchantments.ModEnchantments;
import net.fenn7.thatchermod.entity.ModEntities;
import net.fenn7.thatchermod.item.ModItems;
import net.fenn7.thatchermod.network.ModPackets;
import net.fenn7.thatchermod.particle.ModParticles;
import net.fenn7.thatchermod.screen.ModScreenHandlers;
import net.fenn7.thatchermod.util.ModPredicates;
import net.fenn7.thatchermod.util.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThatcherMod {

    public static final String MOD_ID = "thatchermod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("subscribe to bruno powroznik");

        ModBlocks.registerModBlocks();
        ModEntities.registerModEntities();
        ModItems.registerModItems();
        ModEffects.registerModEffects();
        ModPredicates.registerAllPredicates();

        ModParticles.registerParticles();
        ModEnchantments.registerModEnchantments();

        ModBlockEntities.registerModBlockEntities();
        ModScreenHandlers.registerScreenHandlers();

        ModRegistries.registerAll();
        ModCallbacks.registerAllEvents();
        ModCommands.registerAllCommands();
        ModEvents.registerAllEvents();
        ModPackets.registerC2SPackets();
    }
}
