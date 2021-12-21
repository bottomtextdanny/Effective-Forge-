package com.bottomtextdanny.effective_fg.client;

import com.bottomtextdanny.effective_fg.client.world.WaterfallCloudGenerators;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;

public class WorldTickHandler {

    public static void worldTickLast(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isPaused()) {
            WaterfallCloudGenerators.tick();
        }
    }
}
