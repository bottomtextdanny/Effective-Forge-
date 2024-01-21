package bottomtextdanny.effective_fg.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class RippleParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    public RippleParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteProvider) {
        super(world, x, y, z, xd, yd, zd);

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.spriteProvider = spriteProvider;

        int scaleAgeModifier = 1 + new Random().nextInt(10);
        this.quadSize *= 1.2f + random.nextFloat() / 10f * scaleAgeModifier;
        this.lifetime = 10 + new Random().nextInt(scaleAgeModifier);
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
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        this.setSpriteFromAge(spriteProvider);

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

        Vector3f Vector3f = new Vector3f(-1.0F, -1.0F, 0.0F);
        Vector3f = quaternion2.transform(Vector3f);
        Vector3f[] Vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float j = this.getQuadSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            Quaternionf quaternion = new Quaternionf();
            quaternion.rotateX(Mth.PI / 2f);
            Vector3f Vector3f2 = Vector3fs[k];
            Vector3f2 = quaternion.transform(Vector3f2);
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
}
