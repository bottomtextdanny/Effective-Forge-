package bottomtextdanny.effective_fg.particle_manager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ParticleManager<T extends ParticleData> {
	public final T defaultData;
	final ImmutableSet<ResourceLocation> textures;
	final MutableSpriteSet spriteSet = new MutableSpriteSet();
	private final ParticleCreator<T> creator;

	public ParticleManager(T defaultData, List<ResourceLocation> textures, ParticleCreator<T> creator) {
		super();
		this.defaultData = defaultData;
		this.textures = ImmutableSet.copyOf(textures);
		this.creator = creator;
	}

	public void create(T data, ClientLevel level, double x, double y, double z, double xDelta, double yDelta, double zDelta) {
		Minecraft.getInstance().particleEngine.add(creator.createParticle(data, spriteSet, level, x, y, z, xDelta, yDelta, zDelta));
	}

	public void create(ClientLevel level, double x, double y, double z, double xDelta, double yDelta, double zDelta) {
		Minecraft.getInstance().particleEngine.add(creator.createParticle(defaultData, spriteSet, level, x, y, z, xDelta, yDelta, zDelta));
	}

	public Particle createParticle(T data, ClientLevel level, double x, double y, double z, double xDelta, double yDelta, double zDelta) {
		return creator.createParticle(data, spriteSet, level, x, y, z, xDelta, yDelta, zDelta);
	}

	public Particle createParticle(ClientLevel level, double x, double y, double z, double xDelta, double yDelta, double zDelta) {
		return creator.createParticle(defaultData, spriteSet, level, x, y, z, xDelta, yDelta, zDelta);
	}

	public SpriteSet spriteSet() {
		return spriteSet;
	}

	public int sprites() {
		return textures.size();
	}
}
