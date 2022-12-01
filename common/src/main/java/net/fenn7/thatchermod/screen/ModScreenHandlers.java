package net.fenn7.thatchermod.screen;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlers {

    private static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLER_TYPES = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.MENU_KEY);

    public static final RegistrySupplier<ScreenHandlerType<ThatcherismAltarScreenHandler>> THATCHERISM_ALTAR_SCREEN_HANDLER = SCREEN_HANDLER_TYPES.register(
            "thatcherism_altar",
            () -> new ScreenHandlerType<>(ThatcherismAltarScreenHandler::new)
    );

    public static final RegistrySupplier<ScreenHandlerType<GrenadeLauncherScreenHandler>> GRENADE_LAUNCHER_SCREEN_HANDLER = SCREEN_HANDLER_TYPES.register(
            "grenade_launcher",
            () -> new ScreenHandlerType<>(GrenadeLauncherScreenHandler::new)
    );

    public static void registerScreenHandlers() {
        ThatcherMod.LOGGER.debug("Registering Screen Handlers for " + ThatcherMod.MOD_ID + " ...");
        SCREEN_HANDLER_TYPES.register();
    }
}
