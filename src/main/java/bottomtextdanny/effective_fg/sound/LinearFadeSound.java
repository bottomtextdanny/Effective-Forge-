package bottomtextdanny.effective_fg.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class LinearFadeSound extends AbstractSoundInstance implements TickableSoundInstance {
	private boolean stopped;
	public final float range;
	public final float sourceVolume;

	public LinearFadeSound(ResourceLocation sound, SoundSource source, float range, float sourceVolume, double x, double y, double z) {
		super(sound, source, RandomSource.create());
		float dist = (float) Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().distanceTo(new Vec3(x, y, z));
		looping = false;
		delay = 0;
		relative = false;
		this.range = range;
		this.sourceVolume = sourceVolume;
		attenuation = Attenuation.NONE;
		volume = 0.001F;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void tick() {
		float dist = (float) Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().distanceTo(new Vec3(x, y, z));

		if (volume == 0.0F) stop();
		if (dist < range) volume = sourceVolume * Mth.square(1.0F - dist / range);
		else volume = 0.0F;
	}

	@Override
	public boolean isStopped() {
		return this.stopped;
	}

	protected final void stop() {
		this.stopped = true;
		this.looping = false;
	}
}
