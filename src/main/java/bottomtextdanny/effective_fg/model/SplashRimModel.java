package bottomtextdanny.effective_fg.model;

import bottomtextdanny.effective_fg.util.ParticleModel;
import bottomtextdanny.effective_fg.util.ParticleVoxel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class SplashRimModel extends ParticleModel {
    private final ParticleVoxel splash;

    public SplashRimModel() {
        this.texWidth = 48;
        this.texHeight = 56;

        this.splash = new ParticleVoxel(this);
        this.splash.setPos(6.0F, 0.0F, -6.0F);
        this.splash.texOffs(0, 28).addBox(-12.0F, -16.0F, 0.0F, 12.0F, 16.0F, 12.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexBuffer, int light, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha) {
        this.splash.render(poseStack, vertexBuffer, light, u0, u1, v0, v1, red, green, blue, alpha);
    }
}
