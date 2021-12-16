package com.bottomtextdanny.effective_fg.client.render.particle_support;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

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
