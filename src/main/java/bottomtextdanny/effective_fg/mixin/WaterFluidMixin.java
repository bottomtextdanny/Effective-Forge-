package bottomtextdanny.effective_fg.mixin;

import bottomtextdanny.effective_fg.EffectiveFg;
import bottomtextdanny.effective_fg.tables.EffectiveFgParticles;
import bottomtextdanny.effective_fg.util.EffectUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {

    @Inject(method = "animateTick", at = @At("TAIL"))
    protected void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random, CallbackInfo ci) {
        if (!(level instanceof ClientLevel)) return;

        ClientLevel clientLevel = (ClientLevel) level;

        if (EffectUtil.shouldSplashie(clientLevel, pos.above(), fluidState)) {
            Vec3 vec3d = fluidState.getFlow(level, pos);
            int amount = random.nextInt(EffectiveFg.config().flowingWaterSplashiesAbundance.get());

            for (int i = 0; i <= amount; i++) {
                level.addParticle(ParticleTypes.SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.x * random.nextFloat(), random.nextFloat() / 10f, vec3d.z * random.nextFloat());
            }
        }

        if (EffectUtil.shouldRipple(clientLevel, pos)) {
            int amount = random.nextInt(EffectiveFg.config().rainRippleAbundance.get());

            for (int i = 0; i <= amount; i++) {
                EffectiveFgParticles.RIPPLE.create(clientLevel, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 0.9f, pos.getZ() + .5 + random.nextGaussian() / 2f, 0f, 0f, 0f);
            }
        }
    }
}
