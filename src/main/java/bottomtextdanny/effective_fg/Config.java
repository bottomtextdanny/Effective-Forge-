package bottomtextdanny.effective_fg;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
	public final ForgeConfigSpec.BooleanValue splashes;
	public final ForgeConfigSpec.DoubleValue waterSplashAlpha;
	public final ForgeConfigSpec.BooleanValue cascades;
	public final ForgeConfigSpec.DoubleValue cascadeVolumeMultiplier;
	public final ForgeConfigSpec.DoubleValue cascadeSoundRange;
	public final ForgeConfigSpec.DoubleValue cascadeParticleAmountMultiplier;
	public final ForgeConfigSpec.IntValue rainRippleAbundance;
	public final ForgeConfigSpec.IntValue flowingWaterSplashiesAbundance;

	public Config(ForgeConfigSpec.Builder builder) {
		builder.push("Entity Splash");
		builder.comment("Generates Entity Splashes.");
		splashes = builder.define("splashes", true);
		builder.comment("Defines Water Splash Transparent Channel.");
		waterSplashAlpha = builder.defineInRange("splashAlpha", 0.6, 0.0, 1.0);
		builder.pop();

		builder.push("Cascades");
		builder.comment("Processes Cascades");
		cascades = builder.define("cascades", true);
		builder.comment("Defines Cascade Sound Volume.");
		cascadeVolumeMultiplier = builder.defineInRange("cascade volume", 1.0, 0.0, 100.0);
		builder.comment("Defines Cascade Sound Reach, Measured By Blocks.");
		cascadeSoundRange = builder.defineInRange("cascade sound range", 30.0, 0.0, 256.0);
		builder.comment("Defines Cascade Particle Amount.");
		cascadeParticleAmountMultiplier = builder.defineInRange("cascade particles", 1.0, 0.0, 100.0);
		builder.pop();

		builder.push("Water Effects");
		builder.comment("""
			Defines Ripple Particle Amount Display On Calm Water.
			Zero Means No Ripple Particles.""");
		rainRippleAbundance = builder.defineInRange("ripple abundance", 1, 0, 256);
		builder.comment("""
			Defines Splash Particle Display Amount Around Flowing Water.
			Zero Means No Splash Particles.""");
		flowingWaterSplashiesAbundance = builder.defineInRange("splash abundance", 50, 0, 65536);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
	}
}
