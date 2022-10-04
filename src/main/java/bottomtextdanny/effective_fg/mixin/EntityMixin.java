package bottomtextdanny.effective_fg.mixin;

import bottomtextdanny.effective_fg.EffectiveFg;
import bottomtextdanny.effective_fg.util.EffectUtil;
import bottomtextdanny.effective_fg.particle_manager.SplashParticleData;
import bottomtextdanny.effective_fg.tables.EffectiveFgParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public Level level;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract double getY();

    @Shadow public abstract float getBbWidth();

    @Shadow @Final protected Random random;

    @Shadow public abstract List<Entity> getPassengers();

    @Shadow @Nullable public abstract Entity getFirstPassenger();

    @Shadow protected boolean onGround;

    @Shadow public abstract boolean isInWater();

    @Shadow public abstract boolean isInLava();

    @Shadow public abstract BlockPos blockPosition();

    @Shadow public abstract Vec3 getDeltaMovement();

    @Inject(method = "doWaterSplashEffect", at = @At("TAIL"))
    protected void onSwimmingStart(CallbackInfo callbackInfo) {
        if (!(this.level instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) this.level;
        Entity entity = !this.getPassengers().isEmpty() && this.getFirstPassenger() != null ? this.getFirstPassenger() : (Entity) (Object) this;
        float f = entity == (Object) this ? 0.2f : 0.9f;
        Vec3 vec3d = entity.getDeltaMovement();
        float g = Math.min(1.0f, (float) Math.sqrt(vec3d.x * vec3d.x * (double) 0.2f + vec3d.y * vec3d.y + vec3d.z * vec3d.z * (double) 0.2f) * f);
        if (g > EffectiveFg.SPLASH_SPEED_WATER_THRESHOLD) {
            for (int i = -10; i < 10; i++) {
                BlockPos pos = new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ());
                if (level.getBlockState(pos).getFluidState().isSource()
                    && level.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i + 1, this.getZ())).isAir()) {
                    float width = EffectUtil.splashWidth(entity);
                    float height = EffectUtil.splashHeight(width, entity);

                    if (width > 0.0F) {

                        EffectiveFgParticles.SPLASH.create(
                            new SplashParticleData(width, height), level,
                            this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), 0, 0, 0
                        );
                    }

                    EffectUtil.splashSound(this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), g);
                    break;
                }
            }
        } else {
            for (int i = 0; i < this.getBbWidth() * EffectiveFg.SPLASH_WATER_DROPLET_FACTOR; i++) {
                Random random = this.random;

                EffectiveFgParticles.DROPLET.create((ClientLevel) level, this.getX() + random.nextGaussian() * this.getBbWidth() / 5f, this.getY(), this.getZ() + random.nextGaussian() * this.getBbWidth() / 5f, random.nextGaussian() / 15f, random.nextFloat() / 2.5f, random.nextGaussian() / 15f);
            }

            EffectUtil.smallSplashSound(this.getX(), this.getY(), this.getZ(), g);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo callbackInfo) {
        if (!(this.level instanceof ClientLevel)) return;

        ClientLevel level = (ClientLevel) this.level;
        if (!this.onGround && !this.isInWater() && !this.isInLava() && level.getBlockState(this.blockPosition().offset(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z)).getBlock() == Blocks.LAVA) {
            Entity entity = !this.getPassengers().isEmpty() && this.getFirstPassenger() != null ? this.getFirstPassenger() : (Entity) (Object) this;
            float f = entity == (Object) this ? 0.2f : 0.9f;
            Vec3 vec3d = entity.getDeltaMovement();
            float g = Math.min(1.0f, (float) Math.sqrt(vec3d.x * vec3d.x * (double) 0.2f + vec3d.y * vec3d.y + vec3d.z * vec3d.z * (double) 0.2f) * f);
            if (g > EffectiveFg.SPLASH_SPEED_LAVA_THRESHOLD) {
                for (int i = -10; i < 10; i++) {
                    if (level.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ())).getBlock() == Blocks.LAVA
                        && level.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ())).getFluidState().isSource()
                        && level.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i + 1, this.getZ())).isAir()) {
                        float width = EffectUtil.splashWidth(entity);
                        float height = EffectUtil.splashHeight(width, entity);

                        if (width > 0.0F) {
                            EffectiveFgParticles.LAVA_SPLASH.create(
                                new SplashParticleData(width, height), level,
                                this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), 0, 0, 0
                            );
                        }

                        level.playLocalSound(this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.AMBIENT, 1.0f, 0.8f, false);

                        break;
                    }
                }
            }
        }
    }
}
