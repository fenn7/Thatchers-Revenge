package net.fenn7.thatchermod.sound;

import net.fenn7.thatchermod.ThatcherMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModSounds {

    public static final SoundEvent THATCHER_POSSESSION = registerSound("thatcher_possession");

    public static SoundEvent registerSound(String name) {
        Identifier id = new Identifier(ThatcherMod.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }
}
