package bottomtextdanny.effective_fg.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public abstract class ParticleModel {
    public int texWidth, texHeight;

    public int getTexWidth() {
        return texWidth;
    }

    public int getTexHeight() {
        return texHeight;
    }

    public abstract void renderToBuffer(PoseStack poseStack, VertexConsumer vertexBuffer, int light, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha);
}
