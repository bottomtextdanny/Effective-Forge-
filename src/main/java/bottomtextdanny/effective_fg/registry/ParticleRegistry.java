package bottomtextdanny.effective_fg.registry;

import bottomtextdanny.effective_fg.particle.*;
import bottomtextdanny.effective_fg.particletype.SplashParticleOptions;
import bottomtextdanny.effective_fg.EffectiveFg;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EffectiveFg.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> ENTRIES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EffectiveFg.ID);

    public static final RegistryObject<SimpleParticleType> DROPLET = defer("droplet");
    public static final RegistryObject<ParticleType<SplashParticleOptions>> LAVA_SPLASH = deferSplash("lava_splash");
    public static final RegistryObject<SimpleParticleType> RIPPLE = defer("ripple");
    public static final RegistryObject<ParticleType<SplashParticleOptions>> SPLASH = deferSplash("splash");
    public static final RegistryObject<SimpleParticleType> WATERFALL_CLOUD = defer("waterfall_cloud");

    private static RegistryObject<SimpleParticleType> defer(String name) {
        return ENTRIES.register(name, () -> new SimpleParticleType(true));
    }

    private static RegistryObject<ParticleType<SplashParticleOptions>> deferSplash(String name) {
        return ENTRIES.register(name, () -> new ParticleType<SplashParticleOptions>(true, SplashParticleOptions.DESERIALIZER) {
            private final Codec<SplashParticleOptions> codec = SplashParticleOptions.codec(this);

            @Override
            public Codec<SplashParticleOptions> codec() {
                return codec;
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;

        if (DROPLET.isPresent()) {
            particleEngine.register(DROPLET.get(), DropletParticle.Factory::new);
        }

        if (RIPPLE.isPresent()) {
            particleEngine.register(RIPPLE.get(), RippleParticle.Factory::new);
        }

        if (LAVA_SPLASH.isPresent()) {
            particleEngine.register(LAVA_SPLASH.get(), LavaSplashParticle.Factory::new);
        }

        if (SPLASH.isPresent()) {
            particleEngine.register(SPLASH.get(), SplashParticle.Factory::new);
        }

        if (WATERFALL_CLOUD.isPresent()) {
            particleEngine.register(WATERFALL_CLOUD.get(), WaterfallCloudParticle.Factory::new);
        }
    }
}
