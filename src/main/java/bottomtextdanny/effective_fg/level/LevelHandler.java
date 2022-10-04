package bottomtextdanny.effective_fg.level;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;

public class LevelHandler {

    public static void levelTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isPaused()) {
            WaterfallCloudGenerators.tick();
        }
    }
}
