package bottomtextdanny.effective_fg.particle;

import bottomtextdanny.effective_fg.tables.EffectiveFgParticles;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DropletParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public DropletParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteProvider) {
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        ClientLevel level = this.level;

        if (this.onGround || (this.age > 5 && level.getBlockState(BlockPos.containing(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER)) {
            this.remove();
        }

        if (level.getBlockState(BlockPos.containing(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER && level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
            for (int i = 0; i > -10; i--) {
                if (level.getBlockState(BlockPos.containing(this.x, Math.round(this.y) + i, this.z)).getBlock() == Blocks.WATER && level.getBlockState(BlockPos.containing(this.x, Math.round(this.y) + i, this.z)).getFluidState().isSource() && level.getBlockState(BlockPos.containing(this.x, Math.round(this.y) + i + 1, this.z)).isAir()) {
                    EffectiveFgParticles.RIPPLE.create(level,
                        this.x, Math.round(this.y) + i + 0.9f, this.z,
                        0, 0, 0);
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
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {

        Vec3 vec3d = camera.getPosition();
        float f = (float) (Mth.lerp(tickDelta, this.xo, this.x) - vec3d.x());
        float g = (float) (Mth.lerp(tickDelta, this.yo, this.y) - vec3d.y());
        float h = (float) (Mth.lerp(tickDelta, this.zo, this.z) - vec3d.z());
        Quaternionf quaternion2;
        if (this.roll == 0.0F) {
            quaternion2 = camera.rotation();
        } else {
            quaternion2 = new Quaternionf(camera.rotation());
            float i = Mth.lerp(tickDelta, this.oRoll, this.roll);
            quaternion2.mul(Axis.ZP.rotationDegrees(i));
        }

        Vector3f vec3f = new Vector3f(-1.0F, -1.0F, 0.0F);
        vec3f = quaternion2.transform(vec3f);
        Vector3f[] Vec3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)};
        float j = this.getQuadSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            Vector3f Vec3f2 = Vec3fs[k];
            Vec3f2 = quaternion2.transform(Vec3f2);
            Vec3f2.mul(j);
            Vec3f2.add(f, g, h);
        }

        float minU = this.getU0();
        float maxU = this.getU1();
        float minV = this.getV0();
        float maxV = this.getV1();
        int l = this.getLightColor(tickDelta);

        vertexConsumer.vertex(Vec3fs[0].x(), Vec3fs[0].y(), Vec3fs[0].z()).uv(maxU, maxV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vec3fs[1].x(), Vec3fs[1].y(), Vec3fs[1].z()).uv(maxU, minV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vec3fs[2].x(), Vec3fs[2].y(), Vec3fs[2].z()).uv(minU, minV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();
        vertexConsumer.vertex(Vec3fs[3].x(), Vec3fs[3].y(), Vec3fs[3].z()).uv(minU, maxV).color(rCol, gCol, bCol, alpha).uv2(l).endVertex();

    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new DropletParticle(level, x, y, z, xd, yd, zd, this.spriteProvider);
        }
    }
}
