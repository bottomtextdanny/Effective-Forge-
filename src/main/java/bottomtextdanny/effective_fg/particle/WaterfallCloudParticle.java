package bottomtextdanny.effective_fg.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.Blocks;

public class WaterfallCloudParticle extends TextureSheetParticle {
    private int invisibleTimer;

    public WaterfallCloudParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteProvider) {
        super(level, x, y, z, xd, yd, zd);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.lifetime = 500;
        this.quadSize *= 3.0f + random.nextFloat();
        this.setSpriteFromAge(spriteProvider);

        this.alpha = 0f;

        this.invisibleTimer = 1;
        this.setSprite(spriteProvider.get(level.random));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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

            if (this.onGround || (this.age > 10 && this.level.getBlockState(BlockPos.containing(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER)) {
                this.alpha -= 0.1f;
                this.xd *= 0.5f;
                this.yd *= 0.5f;
                this.zd *= 0.5f;
            }

            if (this.alpha <= 0) {
                this.remove();
            }

            if (this.level.getBlockState(BlockPos.containing(this.x, this.y + this.yd, this.z)).getBlock() == Blocks.WATER && this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
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
}
