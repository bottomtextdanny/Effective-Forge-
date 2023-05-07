package bottomtextdanny.effective_fg.mixin.integration.rubidium;


import bottomtextdanny.effective_fg.EffectiveFg;
import bottomtextdanny.effective_fg.level.WaterfallCloudGenerators;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidRenderer.class, remap = false)
public class FluidRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void renderFluid(BlockAndTintGetter level, FluidState fluidState, BlockPos pos, BlockPos offset, ChunkModelBuilder buffers, CallbackInfoReturnable<Boolean> cir)
    {
        WaterfallCloudGenerators.onRenderFluid(level, pos, fluidState);
    }
}