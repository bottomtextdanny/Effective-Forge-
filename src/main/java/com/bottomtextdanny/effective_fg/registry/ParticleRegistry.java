package com.bottomtextdanny.effective_fg.registry;

import com.bottomtextdanny.effective_fg.EffectiveFg;
import com.bottomtextdanny.effective_fg.client.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
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
    public static final RegistryObject<SimpleParticleType> LAVA_SPLASH = defer("lava_splash");
    public static final RegistryObject<SimpleParticleType> RIPPLE = defer("ripple");
    public static final RegistryObject<SimpleParticleType> SPLASH = defer("splash");
    public static final RegistryObject<SimpleParticleType> WATERFALL_CLOUD = defer("waterfall_cloud");

    private static RegistryObject<SimpleParticleType> defer(String name) {
        return ENTRIES.register(name, () -> new SimpleParticleType(true));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
        if (DROPLET.isPresent()) {
            Minecraft.getInstance().particleEngine.register(DROPLET.get(), DropletParticle.Factory::new);
        }

        if (RIPPLE.isPresent()) {
            Minecraft.getInstance().particleEngine.register(RIPPLE.get(), RippleParticle.Factory::new);
        }

        if (LAVA_SPLASH.isPresent()) {
            Minecraft.getInstance().particleEngine.register(LAVA_SPLASH.get(), LavaSplashParticle.Factory::new);
        }

        if (SPLASH.isPresent()) {
            Minecraft.getInstance().particleEngine.register(SPLASH.get(), SplashParticle.Factory::new);
        }

        if (WATERFALL_CLOUD.isPresent()) {
            Minecraft.getInstance().particleEngine.register(WATERFALL_CLOUD.get(), WaterfallCloudParticle.Factory::new);
        }
    }
}
