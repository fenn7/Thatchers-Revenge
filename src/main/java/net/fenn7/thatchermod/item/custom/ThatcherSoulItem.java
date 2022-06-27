package net.fenn7.thatchermod.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;

public class ThatcherSoulItem extends Item {
    public ThatcherSoulItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack){
        return UseAction.DRINK;
        outputRandomNumber(user);
    }

    private void outputRandomNumber(PlayerEntity player) {
        player.sendMessage(Text.literal("AERRRAUUUUGHAAAAAAAARRRRRRRRRRAAAAAAAAGHHHHHHHHAAAAAAAAA!!!!!!!"));
    }

}
