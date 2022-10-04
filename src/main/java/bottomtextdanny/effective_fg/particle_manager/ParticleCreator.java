package bottomtextdanny.effective_fg.particle_manager;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;

import javax.annotation.Nullable;

public interface ParticleCreator<E extends ParticleData> {
	Particle createParticle(E data, SpriteSet sprites, ClientLevel level, double x, double y, double z, double xDelta, double yDelta, double zDelta);
}
