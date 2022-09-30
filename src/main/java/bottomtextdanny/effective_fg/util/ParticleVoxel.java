package bottomtextdanny.effective_fg.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiConsumer;

public class ParticleVoxel {
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public boolean mirror;
    public boolean visible = true;
    public BiConsumer<ParticleVoxel, PoseStack> renderCallback;
    public float defaultAngleX, defaultAngleY, defaultAngleZ, defaultOffsetX, defaultOffsetY, defaultOffsetZ, defaultSizeX, defaultSizeY, defaultSizeZ;
    public int textureOffsetX, textureOffsetY;
    public float texWidth, texHeight;
    public float scaleX, scaleY, scaleZ;
    public int index = -1;
    ParticleVoxel parent;
    public final ObjectList<ModelBox> cubeList = new ObjectArrayList<>();
    public final ObjectList<ParticleVoxel> childModels = new ObjectArrayList<>();

    public ParticleVoxel(ParticleModel model) {
        this.setScale(1.0F, 1.0F, 1.0F);
        this.setTexSize(model.getTexWidth(), model.getTexHeight());
    }

    @SuppressWarnings("unchecked")
    public <T extends ParticleVoxel> T callback(BiConsumer<ParticleVoxel, PoseStack> cons) {
        this.renderCallback = cons;
        return (T)this;
    }

    public void translateRotateWithParents(PoseStack matrixStackIn) {
        if (parent != null) {
            parent.translateRotateWithParents(matrixStackIn);
        }

        matrixStackIn.translate((double)(this.x / 16.0F), (double)(this.y / 16.0F), (double)(this.z / 16.0F));
        if (this.zRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(this.yRot));
        }

        if (this.xRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(this.xRot));
        }
    }

    public void addChild(ParticleVoxel renderer) {
        this.childModels.add(renderer);
        renderer.parent = this;
    }

    public void setPos(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.x = rotationPointXIn;
        this.y = rotationPointYIn;
        this.z = rotationPointZIn;
    }

    public void setRotationAngle(float rotationAngleXIn, float rotationAngleYIn, float rotationAngleZIn) {
        this.xRot = rotationAngleXIn;
        this.yRot = rotationAngleYIn;
        this.zRot = rotationAngleZIn;
    }

    public void setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public void getDefaultState() {
        defaultAngleX = this.xRot;
        defaultAngleY = this.yRot;
        defaultAngleZ = this.zRot;

        defaultOffsetX = this.x;
        defaultOffsetY = this.y;
        defaultOffsetZ = this.z;

        defaultSizeX = this.scaleX;
        defaultSizeY = this.scaleY;
        defaultSizeZ = this.scaleZ;
    }

    public void setDefaultState() {
        this.xRot = defaultAngleX;
        this.yRot = defaultAngleY;
        this.zRot = defaultAngleZ;

        this.x = defaultOffsetX;
        this.y = defaultOffsetY;
        this.z = defaultOffsetZ;

        setScale(defaultSizeX, defaultSizeY, defaultSizeZ);
    }


    public ParticleVoxel texOffs(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }


    public ParticleVoxel setTexSize(int textureWidthIn, int textureHeightIn) {
        this.texWidth = (float)textureWidthIn;
        this.texHeight = (float)textureHeightIn;
        return this;
    }


    public ParticleVoxel addBox(String partName, float x, float y, float z, int width, int height, int depth, float delta, int texX, int texY) {
        this.texOffs(texX, texY);
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, (float)width, (float)height, (float)depth, delta, delta, delta, this.mirror, false);
        return this;
    }

    public ParticleVoxel addBox(float x, float y, float z, float width, float height, float depth) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, this.mirror, false);
        return this;
    }

    public ParticleVoxel addBox(float x, float y, float z, float width, float height, float depth, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, mirrorIn, false);
        return this;
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror, false);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror, false);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, mirrorIn, false);
    }

    private void addBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, boolean p_228305_13_) {
        this.cubeList.add(new ModelBox(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, this.texWidth, this.texHeight));
    }

    public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, float u0, float u1, float v0, float v1) {
        this.render(matrixStackIn, bufferIn, packedLightIn, u0, u1, v0, v1, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha) {
        if (this.visible) {
            matrixStackIn.pushPose();
            this.translateRotate(matrixStackIn);
            matrixStackIn.scale(this.scaleX, this.scaleY, this.scaleZ);
            if (renderCallback != null) {
                renderCallback.accept(this, matrixStackIn);
            }
            if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
                this.doRender(matrixStackIn.last(), bufferIn, packedLightIn, u0, u1, v0, v1, red, green, blue, alpha);

                for (ParticleVoxel modelrenderer : this.childModels) {
                    modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, red, green, blue, alpha);
                }
            }
            matrixStackIn.popPose();
        }
    }

    public void translateRotate(PoseStack matrixStackIn) {
        matrixStackIn.translate((double)(this.x / 16.0F), (double)(this.y / 16.0F), (double)(this.z / 16.0F));
        if (this.zRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(this.yRot));
        }

        if (this.xRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(this.xRot));
        }

    }

    private void doRender(PoseStack.Pose matrixEntryIn, VertexConsumer bufferIn, int packedLightIn, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = matrixEntryIn.pose();
        Matrix3f matrix3f = matrixEntryIn.normal();

        for (ModelBox modelrenderer$modelbox : this.cubeList) {
            for (TexturedQuad modelrenderer$texturedquad : modelrenderer$modelbox.quads) {
                Vector3f vector3f = modelrenderer$texturedquad.normal.copy();
                vector3f.transform(matrix3f);
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();

                for(int i = 0; i < 4; ++i) {

                    ParticleVoxel.PositionTextureVertex positiontexturevertex = modelrenderer$texturedquad.vertexPlusWidthositions[i];
                    float f3 = positiontexturevertex.position.x() / 16.0F;
                    float f4 = positiontexturevertex.position.y() / 16.0F;
                    float f5 = positiontexturevertex.position.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
                    vector4f.transform(matrix4f);

                    float finalU = ((u1 - u0) * positiontexturevertex.textureU) + u0;
                    float finalV = ((v1 - v0) * positiontexturevertex.textureV) + v0;
                    bufferIn
                            .vertex(vector4f.x(), vector4f.y(), vector4f.z())
                            .uv(finalU, finalV)
                            .color(red, green, blue, alpha)
                            .uv2(packedLightIn)
                            .endVertex();
                }

                for(int i = 0; i < 4; ++i) {
                    ParticleVoxel.PositionTextureVertex positiontexturevertex;
                    if (i % 2 == 0) {
                        positiontexturevertex = modelrenderer$texturedquad.vertexPlusWidthositions[i + 1];
                    } else {
                        positiontexturevertex = modelrenderer$texturedquad.vertexPlusWidthositions[i - 1];
                    }
                    float f3 = positiontexturevertex.position.x() / 16.0F;
                    float f4 = positiontexturevertex.position.y() / 16.0F;
                    float f5 = positiontexturevertex.position.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
                    vector4f.transform(matrix4f);

                    float finalU = ((u1 - u0) * positiontexturevertex.textureU) + u0;
                    float finalV = ((v1 - v0) * positiontexturevertex.textureV) + v0;
                    bufferIn
                            .vertex(vector4f.x(), vector4f.y(), vector4f.z())
                            .uv(finalU, finalV)
                            .color(red, green, blue, alpha)
                            .uv2(packedLightIn)
                            .endVertex();
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class PositionTextureVertex {
        public final Vector3f position;
        public final float textureU;
        public final float textureV;

        public PositionTextureVertex(float x, float y, float z, float texU, float texV) {
            this(new Vector3f(x, y, z), texU, texV);
        }

        public ParticleVoxel.PositionTextureVertex setTextureUV(float texU, float texV) {
            return new ParticleVoxel.PositionTextureVertex(this.position, texU, texV);
        }

        public PositionTextureVertex(Vector3f posIn, float texU, float texV) {
            this.position = posIn;
            this.textureU = texU;
            this.textureV = texV;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class TexturedQuad {
        public final ParticleVoxel.PositionTextureVertex[] vertexPlusWidthositions;
        public final Vector3f normal;

        public TexturedQuad(ParticleVoxel.PositionTextureVertex[] positionsIn, float u0, float v0, float u1, float v1, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn) {
            this.vertexPlusWidthositions = positionsIn;
            float f = 0.0F / texWidth;
            float f1 = 0.0F / texHeight;
            positionsIn[0] = positionsIn[0].setTextureUV(u1 / texWidth - f, v0 / texHeight + f1);
            positionsIn[1] = positionsIn[1].setTextureUV(u0 / texWidth + f, v0 / texHeight + f1);
            positionsIn[2] = positionsIn[2].setTextureUV(u0 / texWidth + f, v1 / texHeight - f1);
            positionsIn[3] = positionsIn[3].setTextureUV(u1 / texWidth - f, v1 / texHeight - f1);
            if (mirrorIn) {
                int i = positionsIn.length;

                for(int j = 0; j < i / 2; ++j) {
                    ParticleVoxel.PositionTextureVertex MoreContentModelRenderer$positiontexturevertex = positionsIn[j];
                    positionsIn[j] = positionsIn[i - 1 - j];
                    positionsIn[i - 1 - j] = MoreContentModelRenderer$positiontexturevertex;
                }
            }

            this.normal = directionIn.step();
            if (mirrorIn) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }

        }
    }

    public static class ModelBox {
        protected final TexturedQuad[] quads;
        public float posX1;
        public float posY1;
        public float posZ1;
        public float posX2;
        public float posY2;
        public float posZ2;

        public ModelBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
            this.posX1 = x;
            this.posY1 = y;
            this.posZ1 = z;
            this.posX2 = x + width;
            this.posY2 = y + height;
            this.posZ2 = z + depth;
            this.quads = new TexturedQuad[6];
            float xPlusWidth = x + width;
            float yPlusHeight = y + height;
            float zPlusDepth = z + depth;
            x = x - deltaX;
            y = y - deltaY;
            z = z - deltaZ;
            xPlusWidth = xPlusWidth + deltaX;
            yPlusHeight = yPlusHeight + deltaY;
            zPlusDepth = zPlusDepth + deltaZ;
            if(mirorIn) {
                float f3 = xPlusWidth;
                xPlusWidth = x;
                x = f3;
            }

            
            PositionTextureVertex modelrenderer$positiontexturevertex7 = new PositionTextureVertex(x, y, zPlusDepth, 0.0F, 8.0F);
            PositionTextureVertex modelrenderer$positiontexturevertex =  new PositionTextureVertex(xPlusWidth, y, zPlusDepth, 8.0F, 0.0F);
            PositionTextureVertex modelrenderer$positiontexturevertex1 = new PositionTextureVertex(xPlusWidth, yPlusHeight, zPlusDepth, 0.0F, 0.0F);
            PositionTextureVertex modelrenderer$positiontexturevertex2 = new PositionTextureVertex(x, yPlusHeight, zPlusDepth, 0.0F, 8.0F);
            PositionTextureVertex modelrenderer$positiontexturevertex3 = new PositionTextureVertex(x, y, z, 8.0F, 8.0F);
            PositionTextureVertex modelrenderer$positiontexturevertex4 = new PositionTextureVertex(xPlusWidth, y, z, 8.0F, 0.0F);
            PositionTextureVertex modelrenderer$positiontexturevertex5 = new PositionTextureVertex(xPlusWidth, yPlusHeight, z, 0.0F, 0.0F);
            PositionTextureVertex modelrenderer$positiontexturevertex6 = new PositionTextureVertex(x, yPlusHeight, z, 0.0F, 8.0F);
            float f4 = (float)texOffX;
            float f5 = (float)texOffX + depth;
            float f6 = (float)texOffX + depth + width;
            float f7 = (float)texOffX + depth + width + width;
            float f8 = (float)texOffX + depth + width + depth;
            float f9 = (float)texOffX + depth + width + depth + width;
            float f10 = (float)texOffY;
            float f11 = (float)texOffY + depth;
            float f12 = (float)texOffY + depth + height;
            this.quads[2] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
            this.quads[3] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex1, modelrenderer$positiontexturevertex2, modelrenderer$positiontexturevertex6, modelrenderer$positiontexturevertex5}, f6, f11, f7, f10, texWidth, texHeight, mirorIn, Direction.UP);
            this.quads[1] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex6, modelrenderer$positiontexturevertex2}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
            this.quads[4] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex, modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex2, modelrenderer$positiontexturevertex1}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
            this.quads[0] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex, modelrenderer$positiontexturevertex1, modelrenderer$positiontexturevertex5}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
            this.quads[5] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex5, modelrenderer$positiontexturevertex6}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
        }
    }
}
