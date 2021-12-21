package com.bottomtextdanny.effective_fg.client.particle;

import com.bottomtextdanny.effective_fg.client.render.model.SplashBottomModel;
import com.bottomtextdanny.effective_fg.client.render.model.SplashModel;
import com.bottomtextdanny.effective_fg.client.render.particle_support.ParticleModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class LavaSplashParticle extends SpriteTexturedParticle {
    private static final ParticleModel WAVE_MODEL = new SplashModel();
    private static final ParticleModel WAVE_BOTTOM_MODEL = new SplashBottomModel();
    private final IAnimatedSprite sprites;
    private float widthMultiplier;
    private float heightMultiplier;
    private int wave1End;
    private int wave2Start;
    private int wave2End;

    protected LavaSplashParticle(ClientWorld level, double x, double y, double z, IAnimatedSprite spriteSet) {
        super(level, x, y, z);
        this.sprites = spriteSet;
        this.gravity = 0.0F;
        this.widthMultiplier = 0f;
        this.heightMultiplier = 0f;
        this.wave1End = 12;
        this.wave2Start = 7;
        this.wave2End = 24;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        if (this.widthMultiplier == 0f) {
            List<Entity> closeEntities = level.getEntities(null, this.getBoundingBox().inflate(5.0f)).stream().filter(entity -> level.getBlockState(entity.blockPosition().offset(entity.getDeltaMovement().x, entity.getDeltaMovement().y, entity.getDeltaMovement().z)).getBlock() == Blocks.LAVA).collect(Collectors.toList());
            closeEntities.sort((o1, o2) -> (int) (o1.position().distanceToSqr(new Vector3d(this.x, this.y, this.z)) - o2.position().distanceToSqr(new Vector3d(this.x, this.y, this.z))));

            if (!closeEntities.isEmpty()) {
                this.widthMultiplier = closeEntities.get(0).getBbWidth() * 2f;
                this.heightMultiplier = (float) Math.max(-closeEntities.get(0).getDeltaMovement().y() * this.widthMultiplier, 0f);

                this.wave1End = 10 + Math.round(widthMultiplier * 1.2f);
                this.wave2Start = 6 + Math.round(widthMultiplier * 0.7f);
                this.wave2End = 20 + Math.round(widthMultiplier * 2.4f);
            } else {
                this.remove();
            }
        }

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.widthMultiplier *= 1.03f;

        if (this.age++ >= this.wave2End) {
            this.remove();
        }

        if (this.age == 1) {
            for (int i = 0; i < this.widthMultiplier * 10f; i++) {
                this.level.addParticle(ParticleTypes.LAVA, this.x + (this.random.nextGaussian() * this.widthMultiplier / 10f), this.y, this.z + (this.random.nextGaussian() * this.widthMultiplier / 10f), random.nextGaussian() / 10f * this.widthMultiplier / 2.5f, random.nextFloat() / 10f + this.heightMultiplier / 2.8f, random.nextGaussian() / 10f * this.widthMultiplier / 2.5f);
            }
        } else if (this.age == wave2Start) {
            for (int i = 0; i < this.widthMultiplier * 5f; i++) {
                this.level.addParticle(ParticleTypes.LAVA, this.x + (this.random.nextGaussian() * this.widthMultiplier / 10f * .5f), this.y, this.z + (this.random.nextGaussian() * this.widthMultiplier / 10f * .5f), random.nextGaussian() / 10f * this.widthMultiplier / 5f, random.nextFloat() / 10f + this.heightMultiplier / 2.2f, random.nextGaussian() / 10f * this.widthMultiplier / 5f);
            }
        }
    }

    @Override
    public void render(IVertexBuilder vertexConsumer, ActiveRenderInfo camera, float tickDelta) {
        int light = 15728880;
      final float yOffset = 0.001F;
        Vector3d vec3d = camera.getPosition();
        float f = (float) (MathHelper.lerp(tickDelta, this.xo, this.x) - vec3d.x());
        float g = (float) (MathHelper.lerp(tickDelta, this.yo, this.y) - vec3d.y()) + yOffset;
        float h = (float) (MathHelper.lerp(tickDelta, this.zo, this.z) - vec3d.z());
        MatrixStack matrixStack = new MatrixStack();
        if (age <= this.wave1End) {
            int frameForFirstSplash = Math.round(((float) this.age / (float) this.wave1End) * 12);

            setSprite(frameForFirstSplash);

            float minU = this.getU0();
            float maxU = this.getU1();
            float minV = this.getV0();
            float maxV = this.getV1();

            matrixStack.pushPose();
            matrixStack.translate(f, g, h);
            matrixStack.scale(widthMultiplier, -heightMultiplier, widthMultiplier);
            WAVE_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }

        if (age >= this.wave2Start) {
            int frameForSecondSplash = Math.round(((float) (this.age - wave2Start) / (float) (this.wave2End - this.wave2Start)) * 12);

            setSprite(frameForSecondSplash);

            float minU = this.getU0();
            float maxU = this.getU1();
            float minV = this.getV0();
            float maxV = this.getV1();

            matrixStack.translate(f, g, h);
            matrixStack.scale(widthMultiplier * 0.5f, -heightMultiplier * 2, widthMultiplier * 0.5f);
            WAVE_MODEL.renderToBuffer(matrixStack, vertexConsumer, light, minU, maxU, minV, maxV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void setSprite(int index) {
        if (!this.removed) {
            this.setSprite(sprites.get(index, 12));
        }
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteProvider) {
            this.spriteSet = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(BasicParticleType parameters, ClientWorld level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new LavaSplashParticle(level, x, y, z, spriteSet);
        }
    }
}
