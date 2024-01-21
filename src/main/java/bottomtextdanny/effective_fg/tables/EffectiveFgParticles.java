package bottomtextdanny.effective_fg.tables;

import bottomtextdanny.effective_fg.particle.*;
import bottomtextdanny.effective_fg.particle_manager.NoParticleData;
import bottomtextdanny.effective_fg.particle_manager.ParticleManager;
import bottomtextdanny.effective_fg.particle_manager.SplashParticleData;
import bottomtextdanny.effective_fg.EffectiveFg;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;

public class EffectiveFgParticles {

    public static final ParticleManager<NoParticleData> DROPLET = new ParticleManager<>(
            NoParticleData.INST, List.of(new ResourceLocation(EffectiveFg.ID, "droplet")),
            (data, sprites, level, x, y, z, xDelta, yDelta, zDelta) -> {
                return new DropletParticle(level, x, y, z, xDelta, yDelta, zDelta, sprites);
            }
    );

    public static final ParticleManager<SplashParticleData> LAVA_SPLASH = new ParticleManager<>(
            new SplashParticleData(1.0F, 1.0F), serial("lava_splash", 13),
            (data, sprites, level, x, y, z, xDelta, yDelta, zDelta) -> {
                return new LavaSplashParticle(level, x, y, z, sprites, data.width, data.height);
            }
    );

    public static final ParticleManager<NoParticleData> RIPPLE = new ParticleManager<>(
            NoParticleData.INST, serial("ripple", 8),
            (data, sprites, level, x, y, z, xDelta, yDelta, zDelta) -> {
                return new RippleParticle(level, x, y, z, xDelta, yDelta, zDelta, sprites);
            }
    );

    public static final ParticleManager<SplashParticleData> SPLASH = new ParticleManager<>(
            new SplashParticleData(1.0F, 1.0F), serial("splash", 13),
            (data, sprites, level, x, y, z, xDelta, yDelta, zDelta) -> {
                return new SplashParticle(level, x, y, z, sprites, data.width, data.height);
            }
    );

    public static final ParticleManager<NoParticleData> WATERFALL_CLOUD = new ParticleManager<>(
            NoParticleData.INST, serial("waterfall_cloud", 12),
            (data, sprites, level, x, y, z, xDelta, yDelta, zDelta) -> {
                return new WaterfallCloudParticle(level, x, y, z, xDelta, yDelta, zDelta, sprites);
            }
    );

    private static List<ResourceLocation> serial(String name, int size) {
        List<ResourceLocation> textures = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            textures.add(new ResourceLocation(EffectiveFg.ID, name + '_' + i));
        }

        return textures;
    }
}
