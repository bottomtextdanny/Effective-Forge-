package com.bottomtextdanny.effective_fg.mixin.client;

import com.bottomtextdanny.effective_fg.client.world.WaterfallCloudGenerators;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
public class BlockRenderManagerMixin {

    @Inject(method = "renderLiquid", at = @At("TAIL"))
    public void renderFluid(BlockPos pos, IBlockDisplayReader world, IVertexBuilder vertexConsumer, FluidState state, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        WaterfallCloudGenerators.tryAddGenerator(world, pos);
    }
}
