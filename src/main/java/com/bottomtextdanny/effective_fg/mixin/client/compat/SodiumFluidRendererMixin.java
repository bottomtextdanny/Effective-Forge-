package com.bottomtextdanny.effective_fg.mixin.client.compat;

import com.bottomtextdanny.effective_fg.client.world.WaterfallCloudGenerators;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuffers;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public class SodiumFluidRendererMixin
{
    @Inject(method = "render", at = @At("HEAD"), remap = false)
    public void renderFluid(IBlockDisplayReader facing, FluidState u1, BlockPos u2, ChunkModelBuffers u3, CallbackInfoReturnable<Boolean> cir) {
        WaterfallCloudGenerators.tryAddGenerator(facing, u2);
    }
}