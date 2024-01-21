package bottomtextdanny.effective_fg.mixin;

import bottomtextdanny.effective_fg.EffectiveFg;
import bottomtextdanny.effective_fg.level.WaterfallCloudGenerators;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {

    @Inject(method = "tesselate", at = @At("TAIL"))
    public void renderFluid(BlockAndTintGetter level, BlockPos pos, VertexConsumer buffer, BlockState state, FluidState fluidState, CallbackInfo ci) {
        WaterfallCloudGenerators.onRenderFluid(level, pos, fluidState);
    }
}
