package net.fenn7.thatchermod.screen;

import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlers {

    public static final ScreenHandlerType<ThatcherismAltarScreenHandler> THATCHERISM_ALTAR_SCREEN_HANDLER = Registry.register(
            Registry.SCREEN_HANDLER,
            new Identifier(ThatcherMod.MOD_ID, "thatcherism_altar"),
            new ScreenHandlerType<>(ThatcherismAltarScreenHandler::new));

    public static void registerScreenHandlers() {
        ThatcherMod.LOGGER.debug("Registering Screen Handlers for " + ThatcherMod.MOD_ID + " ...");
    }
}

