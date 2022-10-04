package bottomtextdanny.effective_fg.particle_manager;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class SplashParticleData implements ParticleData {
	public final float width, height;

	public SplashParticleData(float width, float height) {
		this.width = width;
		this.height = height;
	}
}
