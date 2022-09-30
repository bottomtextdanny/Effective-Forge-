package bottomtextdanny.effective_fg.level;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;

public class LevelTickHandler {

    public static void worldTickLast(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isPaused()) {
            WaterfallCloudGenerators.tick();
        }
    }
}
