package bottomtextdanny.effective_fg.particletype;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class SplashParticleOptions implements ParticleOptions {
	public static final Deserializer<SplashParticleOptions> DESERIALIZER = new Deserializer<SplashParticleOptions>() {
		public SplashParticleOptions fromCommand(ParticleType<SplashParticleOptions> type, StringReader string) throws CommandSyntaxException {
			string.expect(' ');
			float width = string.readFloat();
			string.expect(' ');
			float height = string.readFloat();
			return new SplashParticleOptions(type, width, height);
		}

		public SplashParticleOptions fromNetwork(ParticleType<SplashParticleOptions> type, FriendlyByteBuf stream) {
			return new SplashParticleOptions(type, stream.readFloat(), stream.readFloat());
		}
	};
	public final ParticleType<?> type;
	public final float width, height;

	public SplashParticleOptions(ParticleType<?> type, float width, float height) {
		this.type = type;
		this.width = width;
		this.height = height;
	}

	public static Codec<SplashParticleOptions> codec(ParticleType<SplashParticleOptions> type) {
		return RecordCodecBuilder.create((inst) -> {
			return inst.group(
				Codec.FLOAT.fieldOf("width").forGetter((impl) -> {
					return impl.width;
				}),
				Codec.FLOAT.fieldOf("height").forGetter((impl) -> {
					return impl.height;
				})
			).apply(inst, (width, height) -> new SplashParticleOptions(type, width, height));
		});
	}

	public void writeToNetwork(FriendlyByteBuf stream) {
		stream.writeFloat(width);
		stream.writeFloat(height);
	}

	public String writeToString() {
		return Registry.PARTICLE_TYPE.getKey(this.getType()) + ", width:" + width + ", height:" + height;
	}

	public ParticleType<?> getType() {
		return this.type;
	}
}
