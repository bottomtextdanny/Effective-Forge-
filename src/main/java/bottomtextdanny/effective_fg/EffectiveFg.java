package bottomtextdanny.effective_fg;

import bottomtextdanny.effective_fg.level.LevelHandler;
import bottomtextdanny.effective_fg.particle_manager.ParticleStitcher;
import bottomtextdanny.effective_fg.tables.EffectiveFgParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

@Mod(EffectiveFg.ID)
public class EffectiveFg {
    public static final int WATERFALL_PARTICLE_RARITY = 3;
    public static final float FLOWING_WATER_SHOULD_BEHAVE_AT_HEIGHT = 0.77F;
    public static final int FLOWING_WATER_DROP_RARITY = 50;
    public static final float SPLASH_WATER_VOLUME_FACTOR = 10.0F;
    public static final float SMALL_SPLASH_WATER_VOLUME_FACTOR = 5.0F;
    public static final float SPLASH_WATER_DROPLET_FACTOR = 25.0F;
    public static final float SPLASH_SPEED_WATER_THRESHOLD = 0.1F;
    public static final float SPLASH_SPEED_LAVA_THRESHOLD = 0.05F;
    public static final String ID = "effective_fg";
    private static Config config;
    private static Object particleHandlers;

    public EffectiveFg() {

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> EffectiveFg::setupClient);
    }

    @OnlyIn(Dist.CLIENT)
    private static void setupClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        particleHandlers = new ParticleStitcher(modEventBus, List.of(
            EffectiveFgParticles.DROPLET,
            EffectiveFgParticles.LAVA_SPLASH,
            EffectiveFgParticles.RIPPLE,
            EffectiveFgParticles.SPLASH,
            EffectiveFgParticles.WATERFALL_CLOUD
        ));

        config = new Config(new ForgeConfigSpec.Builder());
        MinecraftForge.EVENT_BUS.addListener(LevelHandler::levelTick);
    }

    @OnlyIn(Dist.CLIENT)
    public static Config config() {
        if (config == null)
            throw new IllegalStateException("Config was called before initialization or has changed since!");
        return config;
    }
}
