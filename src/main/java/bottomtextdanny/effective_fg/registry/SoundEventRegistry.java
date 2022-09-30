package bottomtextdanny.effective_fg.registry;

import bottomtextdanny.effective_fg.EffectiveFg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EffectiveFg.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> ENTRIES = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EffectiveFg.ID);

    public static final RegistryObject<SoundEvent> AMBIENCE_WATERFALL = defer("ambience.waterfall");
    public static final RegistryObject<SoundEvent> AMBIENCE_SPLASH = defer("ambience.splash");
    public static final RegistryObject<SoundEvent> AMBIENCE_SMALL_SPLASH = defer("ambience.small_splash");

    private static RegistryObject<SoundEvent> defer(String path) {
        return ENTRIES.register(path.substring(path.indexOf(".")), () -> new SoundEvent(new ResourceLocation(EffectiveFg.ID, path)));
    }
}
