package net.fenn7.thatchermod.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ThatcherMod.MOD_ID, Registry.SOUND_EVENT_KEY);

    public static final RegistrySupplier<SoundEvent> THATCHER_POSSESSION = SOUND_EVENTS.register(
            "thatcher_possession",
            () -> new SoundEvent(new Identifier(ThatcherMod.MOD_ID, "thatcher_possession"))
    );

    public static final RegistrySupplier<SoundEvent> THATCHER_SUMMONING = SOUND_EVENTS.register(
            "thatcher_summoning",
            () -> new SoundEvent(new Identifier(ThatcherMod.MOD_ID, "thatcher_summoning"))
    );

    public static final RegistrySupplier<SoundEvent> LAST_HEARTBEAT = SOUND_EVENTS.register(
            "last_heartbeat",
            () -> new SoundEvent(new Identifier(ThatcherMod.MOD_ID, "last_heartbeat"))
    );

    public static final RegistrySupplier<SoundEvent> HEART_OVERLOAD = SOUND_EVENTS.register(
            "heart_overload",
            () -> new SoundEvent(new Identifier(ThatcherMod.MOD_ID, "heart_overload"))
    );

    public static void registerModSounds() {
        SOUND_EVENTS.register();
    }
}
