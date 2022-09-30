package bottomtextdanny.effective_fg.util;

import bottomtextdanny.effective_fg.EffectiveFg;
import bottomtextdanny.effective_fg.registry.SoundEventRegistry;
import bottomtextdanny.effective_fg.sound.LinearFadeSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

public final class EffectUtil {

	public static float splashWidth(Entity entity) {
		return entity.getBbWidth() * 2.0F;
	}

	public static float splashHeight(float width, Entity entity) {
		return Math.max(-(float)entity.getDeltaMovement().y() * width, 0.0F);
	}

	public static void splashSound(double x, double y, double z, float mag) {
		Minecraft.getInstance().getSoundManager().play(new LinearFadeSound(SoundEventRegistry.AMBIENCE_SPLASH.get(), SoundSource.AMBIENT, 20 + mag * 40, mag * EffectiveFg.SPLASH_WATER_VOLUME_FACTOR, x, y, z));
	}

	public static void smallSplashSound(double x, double y, double z, float mag) {
		Minecraft.getInstance().getSoundManager().play(new LinearFadeSound(SoundEventRegistry.AMBIENCE_SMALL_SPLASH.get(), SoundSource.AMBIENT, 10 + mag * 15, mag * EffectiveFg.SMALL_SPLASH_WATER_VOLUME_FACTOR, x, y, z));
	}

	public static boolean shouldSplashie(Level level, BlockPos pos, FluidState fluidState) {
		if (EffectiveFg.config().flowingWaterSplashiesAbundance.get() > 0) {
			if (!fluidState.isSource() & fluidState.getOwnHeight() >= EffectiveFg.FLOWING_WATER_SHOULD_BEHAVE_AT_HEIGHT) {
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				for (Direction direction : Direction.values()) {
					mutable.setWithOffset(pos, direction);
					if (direction.getAxis() != Direction.Axis.Y && level.getBlockState(mutable).isAir()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean shouldRipple(Level level, BlockPos pos) {
		if (EffectiveFg.config().rainRippleAbundance.get() > 0 && level.isRaining()) {
			FluidState fluidState = level.getFluidState(pos);
			return fluidState.isSource() && level.getBlockState(pos.above()).isAir();
		}
		return false;
	}

	private EffectUtil() {}
}
