package com.bottomtextdanny.effective_fg.client.particle;


import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class WaterfallCloudParticle extends SpriteTexturedParticle
{
    private final IAnimatedSprite spriteProvider;
    private int invisibleTimer;

    private WaterfallCloudParticle(ClientWorld level, double x, double y, double z, double xd, double yd, double zd, IAnimatedSprite spriteProvider) {
        super(level, x, y, z, xd, yd, zd);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.spriteProvider = spriteProvider;
        this.lifetime = 500;
        this.quadSize *= 3.0f + random.nextFloat();
        this.setSpriteFromAge(spriteProvider);

        this.alpha = 0f;

        this.invisibleTimer = 1;
        this.setSprite(this.spriteProvider.get(level.random));
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.invisibleTimer--;

        if (this.invisibleTimer == 0) {
            this.alpha = 0.5f + random.nextFloat() * 0.25f;
        }

        if (this.invisibleTimer <= 0) {
            this.xo = this.x;
            this.yo = this.y;
            this.zo = this.z;

            if (this.age++ >= this.lifetime) {
                this.alpha -= 0.1f;
                this.xd *= 0.5f;
                this.yd *= 0.5f;
                this.zd *= 0.5f;
            }

            if (this.onGround || (this.age > 10 && this.level.getBlockState(new BlockPos(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER)) {
                this.alpha -= 0.1f;
                this.xd *= 0.5f;
                this.yd *= 0.5f;
                this.zd *= 0.5f;
            }

            if (this.alpha <= 0) {
                this.remove();
            }

            if (this.level.getBlockState(new BlockPos(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER && this.level.getBlockState(new BlockPos(this.x, this.y, this.z)).isAir()) {
                this.xd *= 0.9;
                this.yd *= 0.9;
                this.zd *= 0.9;
            }

            this.xd *= 0.95f;
            this.yd -= 0.02f;
            this.zd *= 0.95f;

            this.move(xd, yd, zd);
        } else {
            this.alpha = 0;
        }
    }

    @Override
    public void render(IVertexBuilder vertexConsumer, ActiveRenderInfo camera, float tickDelta) {
        Vector3d vec3d = camera.getPosition();
        float f = (float) (MathHelper.lerp(tickDelta, this.xo, this.x) - vec3d.x());
        float g = (float) (MathHelper.lerp(tickDelta, this.yo, this.y) - vec3d.y());
        float h = (float) (MathHelper.lerp(tickDelta, this.zo, this.z) - vec3d.z());
        Quaternion quaternion2;
        if (this.roll == 0.0F) {
            quaternion2 = camera.rotation();
        } else {
            quaternion2 = new Quaternion(camera.rotation());
            float i = MathHelper.lerp(tickDelta, this.oRoll, this.roll);
            quaternion2.mul(Vector3f.ZP.rotationDegrees(i));
        }

        Vector3f[] Vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float j = this.getQuadSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            Vector3f Vector3f2 = Vector3fs[k];
            Vector3f2.transform(quaternion2);
            Vector3f2.mul(j);
            Vector3f2.add(f, g, h);
        }

        float minU = this.getU0();
        float maxU = this.getU1();
        float minV = this.getV0();
        float maxV = this.getV1();
        int l = this.getLightColor(tickDelta);

        vertexConsumer.vertex(Vector3fs[0].x(), Vector3fs[0].y(), Vector3fs[0].z()).uv(maxU, maxV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vector3fs[1].x(), Vector3fs[1].y(), Vector3fs[1].z()).uv(maxU, minV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vector3fs[2].x(), Vector3fs[2].y(), Vector3fs[2].z()).uv(minU, minV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vector3fs[3].x(), Vector3fs[3].y(), Vector3fs[3].z()).uv(minU, maxV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
    }
    
    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        private final IAnimatedSprite spriteProvider;

        public Factory(IAnimatedSprite spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(BasicParticleType parameters, ClientWorld level, double x, double y, double z, double xd, double yd, double zd) {
            return new WaterfallCloudParticle(level, x, y, z, xd, yd, zd, this.spriteProvider);
        }
    }
}
