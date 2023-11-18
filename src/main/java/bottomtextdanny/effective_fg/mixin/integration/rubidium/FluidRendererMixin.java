package bottomtextdanny.effective_fg.mixin.integration.rubidium;


import bottomtextdanny.effective_fg.level.WaterfallCloudGenerators;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FluidRenderer.class, remap = false)
public class FluidRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void renderFluid(WorldSlice world, FluidState fluidState, BlockPos blockPos, BlockPos offset, ChunkBuildBuffers buffers, CallbackInfo ci)
    {
        WaterfallCloudGenerators.onRenderFluid(world, blockPos, fluidState);
    }
}