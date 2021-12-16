package com.bottomtextdanny.effective_fg.client;

import com.bottomtextdanny.effective_fg.EffectiveFg;
import com.bottomtextdanny.effective_fg.client.world.WaterfallCloudGenerators;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class WorldTickHandler {

    public static void worldTickLast(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isPaused()) {
            WaterfallCloudGenerators.tick();
        }
    }
}
