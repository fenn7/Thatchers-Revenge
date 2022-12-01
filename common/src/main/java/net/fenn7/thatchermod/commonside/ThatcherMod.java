package net.fenn7.thatchermod.commonside;

import net.fenn7.thatchermod.commonside.block.ModBlockEntities;
import net.fenn7.thatchermod.commonside.block.ModBlocks;
import net.fenn7.thatchermod.commonside.callback.ModCallbacks;
import net.fenn7.thatchermod.commonside.effect.ModEffects;
import net.fenn7.thatchermod.commonside.enchantments.ModEnchantments;
import net.fenn7.thatchermod.commonside.entity.ModEntities;
import net.fenn7.thatchermod.commonside.item.ModItems;
import net.fenn7.thatchermod.commonside.network.ModPackets;
import net.fenn7.thatchermod.commonside.particle.ModParticles;
import net.fenn7.thatchermod.commonside.screen.ModScreenHandlers;
import net.fenn7.thatchermod.commonside.util.ModPredicates;
import net.fenn7.thatchermod.commonside.util.ModRegistries;
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
