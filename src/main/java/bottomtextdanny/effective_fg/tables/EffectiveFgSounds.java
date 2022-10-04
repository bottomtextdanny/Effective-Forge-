package bottomtextdanny.effective_fg.tables;

import bottomtextdanny.effective_fg.EffectiveFg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectiveFgSounds {

    public static final ResourceLocation AMBIENCE_WATERFALL = effectiveLoc("ambience.waterfall");
    public static final ResourceLocation AMBIENCE_SPLASH = effectiveLoc("ambience.splash");
    public static final ResourceLocation AMBIENCE_SMALL_SPLASH = effectiveLoc("ambience.small_splash");

    private static ResourceLocation effectiveLoc(String path) {
        return new ResourceLocation(EffectiveFg.ID, path);
    }
}
