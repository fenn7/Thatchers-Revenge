package net.fenn7.thatchermod.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class LastStandScreen extends Screen {
    protected LastStandScreen(Text title) {
        super(title);
    }

}
