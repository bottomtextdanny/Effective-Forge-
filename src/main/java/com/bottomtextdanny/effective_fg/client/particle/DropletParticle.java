package com.bottomtextdanny.effective_fg.client.particle;

import com.bottomtextdanny.effective_fg.EffectiveFg;
import com.bottomtextdanny.effective_fg.registry.ParticleRegistry;
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

public class DropletParticle extends SpriteTexturedParticle {
    private final IAnimatedSprite sprites;

    private DropletParticle(ClientWorld level, double x, double y, double z, double xd, double yd, double zd, IAnimatedSprite spriteProvider) {
        super(level, x, y, z, xd, yd, zd);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.sprites = spriteProvider;
        this.lifetime = 500;
        this.quadSize = .05f;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        if (this.onGround || (this.age > 5 && this.level.getBlockState(new BlockPos(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER)) {
            this.remove();
        }

        if (this.level.getBlockState(new BlockPos(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER && this.level.getBlockState(new BlockPos(this.x, this.y, this.z)).isAir()) {
            for (int i = 0; i > -10; i--) {
                if (this.level.getBlockState(new BlockPos(this.x, Math.round(this.y) + i, this.z)).getBlock() == Blocks.WATER && this.level.getBlockState(new BlockPos(this.x, Math.round(this.y) + i, this.z)).getFluidState().isSource() && this.level.getBlockState(new BlockPos(this.x, Math.round(this.y) + i + 1, this.z)).isAir()) {
                    this.level.addParticle(ParticleRegistry.RIPPLE.get(), this.x, Math.round(this.y) + i + 0.9f, this.z, 0, 0, 0);
                    break;
                }
            }

            this.remove();
        }

        this.xd *= 0.99f;
        this.yd -= 0.05f;
        this.zd *= 0.99f;

        this.move(xd, yd, zd);
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

        Vector3f vec3f = new Vector3f(-1.0F, -1.0F, 0.0F);
        vec3f.transform(quaternion2);
        Vector3f[] Vector3dfs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)};
        float j = this.getQuadSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            Vector3f Vector3df2 = Vector3dfs[k];
            Vector3df2.transform(quaternion2);
            Vector3df2.mul(j);
            Vector3df2.add(f, g, h);
        }

        float minU = this.getU0();
        float maxU = this.getU1();
        float minV = this.getV0();
        float maxV = this.getV1();
        int l = this.getLightColor(tickDelta);

        vertexConsumer.vertex(Vector3dfs[0].x(), Vector3dfs[0].y(), Vector3dfs[0].z()).uv(maxU, maxV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vector3dfs[1].x(), Vector3dfs[1].y(), Vector3dfs[1].z()).uv(maxU, minV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vector3dfs[2].x(), Vector3dfs[2].y(), Vector3dfs[2].z()).uv(minU, minV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vector3dfs[3].x(), Vector3dfs[3].y(), Vector3dfs[3].z()).uv(minU, maxV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();

    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteProvider;

        public Factory(IAnimatedSprite spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(BasicParticleType parameters, ClientWorld level, double x, double y, double z, double xd, double yd, double zd) {
            return new DropletParticle(level, x, y, z, xd, yd, zd, this.spriteProvider);
        }
    }
}
