package net.fenn7.thatchermod.commonside.util;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

public class ModText {

    public static MutableText literal(String text) {
        return new LiteralText(text);
    }
}
