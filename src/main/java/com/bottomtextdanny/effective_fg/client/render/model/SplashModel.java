package com.bottomtextdanny.effective_fg.client.render.model;

import com.bottomtextdanny.effective_fg.client.render.particle_support.ParticleModel;
import com.bottomtextdanny.effective_fg.client.render.particle_support.ParticleVoxel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public class SplashModel extends ParticleModel
{
    private final ParticleVoxel splash;

    public SplashModel() {
        this.texWidth = 48;
        this.texHeight = 28;

        this.splash = new ParticleVoxel(this);
        this.splash.setPos(6.0F, 0.0F, -6.0F);
        this.splash.texOffs(0, 0).addBox(-12.0F, -16.0F, 0.0F, 12.0F, 16.0F, 12.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack poseStack, IVertexBuilder vertexBuffer, int light, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha) {
        this.splash.render(poseStack, vertexBuffer, light, u0, u1, v0, v1, red, green, blue, alpha);
    }
}
