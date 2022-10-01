package bottomtextdanny.effective_fg.particle;

import bottomtextdanny.effective_fg.EffectiveFg;
import bottomtextdanny.effective_fg.model.SplashBottomModel;
import bottomtextdanny.effective_fg.model.SplashModel;
import bottomtextdanny.effective_fg.model.SplashRimModel;
import bottomtextdanny.effective_fg.util.ParticleModel;
import bottomtextdanny.effective_fg.particletype.SplashParticleOptions;
import bottomtextdanny.effective_fg.registry.ParticleRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SplashParticle extends TextureSheetParticle {
    static final ParticleModel WAVE_MODEL = new SplashModel(58);
    static final ParticleModel WAVE_RIM_MODEL = new SplashRimModel();
    static final ParticleModel WAVE_BOTTOM_MODEL = new SplashBottomModel(58);
    private final SpriteSet sprites;
    private float widthMultiplier;
    private final float heightMultiplier;
    private final int wave1End;
    private final int wave2Start;
    private final int wave2End;
    private final float r;
    private final float g;
    private final float b;

    protected SplashParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, float width, float height) {
        super(level, x, y, z);
        this.sprites = spriteSet;
        this.gravity = 0.0F;
        this.widthMultiplier = width;
        this.heightMultiplier = height;

        int waterColor = BiomeColors.getAverageWaterColor(level, new BlockPos(x, y, z));

        r = (float) (waterColor >> 16 & 0xFF) / 255.0f;
        g = (float) (waterColor >> 8 & 0xFF) / 255.0f;
        b = (float) (waterColor & 0xFF) / 255.0f;

        this.wave1End = 10 + Math.round(widthMultiplier * 1.2f);
        this.wave2Start = 6 + Math.round(widthMultiplier * 0.7f);
        this.wave2End = 20 + Math.round(widthMultiplier * 2.4f);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.widthMultiplier *= 1.03f;
        float widthMultiplier = this.widthMultiplier;

        if (this.age++ >= this.wave2End) {
            this.remove();
        }

        if (this.age == 1) {
            for (int i = 0; i < widthMultiplier * 10f; i++) {
                this.level.addParticle(ParticleRegistry.DROPLET.get(), this.x + (this.random.nextGaussian() * widthMultiplier / 10f), this.y, this.z + (this.random.nextGaussian() * widthMultiplier / 10f), random.nextGaussian() / 10f * widthMultiplier / 2.5f, random.nextFloat() / 10f + this.heightMultiplier / 2.8f, random.nextGaussian() / 10f * widthMultiplier / 2.5f);
            }
        } else if (this.age == wave2Start) {
            for (int i = 0; i < widthMultiplier * 5f; i++) {
                this.level.addParticle(ParticleRegistry.DROPLET.get(), this.x + (this.random.nextGaussian() * widthMultiplier / 10f * .5f), this.y, this.z + (this.random.nextGaussian() * widthMultiplier / 10f * .5f), random.nextGaussian() / 10f * widthMultiplier / 5f, random.nextFloat() / 10f + this.heightMultiplier / 2.2f, random.nextGaussian() / 10f * widthMultiplier / 5f);
            }
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        final float yOffset = 0.001F;
        final float bottomYOffset = -0.1F;
        int light = getLightColor(tickDelta);

        Vec3 vec3d = camera.getPosition();

        float viewX = (float) (Mth.lerp(tickDelta, this.xo, this.x) - vec3d.x());
        float viewY = (float) (Mth.lerp(tickDelta, this.yo, this.y) - vec3d.y()) + yOffset;
        float viewZ = (float) (Mth.lerp(tickDelta, this.zo, this.z) - vec3d.z());

        PoseStack matrixStack = new PoseStack();
        float widthMultiplier = this.widthMultiplier;
        float heightMultiplier = this.heightMultiplier;

        if (age <= this.wave1End) {
            int frameForFirstSplash = Math.round(((float) this.age / (float) this.wave1End) * 12);
            setSprite(frameForFirstSplash);

            float minU = this.getU0();
            float maxU = this.getU1();
            float minV = this.getV0();
            float maxV = this.getV1();

            matrixStack.pushPose();
            matrixStack.translate(viewX, viewY, viewZ);
            matrixStack.scale(widthMultiplier, -heightMultiplier, widthMultiplier);
            WAVE_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, r, g, b, EffectiveFg.config().waterSplashAlpha.get().floatValue());
            WAVE_RIM_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.clear();
            matrixStack.translate(viewX, viewY, viewZ);
            matrixStack.scale(widthMultiplier * 1.2F, -heightMultiplier, widthMultiplier * 1.2F);
            WAVE_BOTTOM_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.clear();
            matrixStack.translate(viewX, viewY + bottomYOffset, viewZ);
            matrixStack.scale(widthMultiplier * 1.2F, -heightMultiplier, widthMultiplier * 1.2F);
            WAVE_BOTTOM_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }

        if (age >= this.wave2Start) {
            int frameForSecondSplash = Math.round(((float) (this.age - wave2Start) / (float) (this.wave2End - this.wave2Start)) * 12);
            setSprite(frameForSecondSplash);

            float minU = this.getU0();
            float maxU = this.getU1();
            float minV = this.getV0();
            float maxV = this.getV1();

            matrixStack.pushPose();
            matrixStack.translate(viewX, viewY, viewZ);

            matrixStack.pushPose();
            matrixStack.scale(widthMultiplier * 0.5f, -heightMultiplier * 2.0f, widthMultiplier * 0.5f);
            WAVE_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, r, g, b, EffectiveFg.config().waterSplashAlpha.get().floatValue());
            WAVE_RIM_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();

            matrixStack.pushPose();
            matrixStack.scale(widthMultiplier * 0.6f, -heightMultiplier * 2.0f, widthMultiplier * 0.6f);
            WAVE_BOTTOM_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();

            matrixStack.pushPose();
            matrixStack.translate(0.0, bottomYOffset, 0.0);
            matrixStack.scale(widthMultiplier * 0.6f, -heightMultiplier * 2.0f, widthMultiplier * 0.6f);
            WAVE_BOTTOM_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();

            matrixStack.popPose();
        }
    }

    public void setSprite(int index) {
        if (!this.removed) {
            this.setSprite(sprites.get(index, 12));
        }
    }
    
    public static class Factory implements ParticleProvider<SplashParticleOptions> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteProvider) {
            this.spriteSet = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SplashParticleOptions parameters, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SplashParticle(level, x, y, z, this.spriteSet, parameters.width, parameters.height);
        }
    }
}
