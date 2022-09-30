package bottomtextdanny.effective_fg.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LinearFadeSound extends AbstractTickableSoundInstance {
	public final float range;
	public final float sourceVolume;

	public LinearFadeSound(SoundEvent sound, SoundSource source, float range, float sourceVolume, double x, double y, double z) {
		super(sound, source);
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
}
