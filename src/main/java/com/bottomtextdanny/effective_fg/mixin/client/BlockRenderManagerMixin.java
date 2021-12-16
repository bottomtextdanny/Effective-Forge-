package com.bottomtextdanny.effective_fg.mixin.client;

import com.bottomtextdanny.effective_fg.client.world.WaterfallCloudGenerators;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderDispatcher.class)
public class BlockRenderManagerMixin {

    @Inject(method = "renderLiquid", at = @At("TAIL"))
    public void renderFluid(BlockPos pos, BlockAndTintGetter world, VertexConsumer vertexConsumer, FluidState state, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {

        BlockState stateDoubleUp = world.getBlockState(pos.offset(0, 2, 0));
        if (world.getBlockState(pos).getBlock() == Blocks.WATER && world.getBlockState(pos).getFluidState().isSource() && world.getBlockState(pos.offset(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(pos.offset(0, 1, 0)).getFluidState().isSource() && world.getBlockState(pos.offset(0, 1, 0)).getFluidState().getOwnHeight() >= 0.77f && stateDoubleUp.is(Blocks.WATER) && !stateDoubleUp.getFluidState().isSource()) {
            if (!WaterfallCloudGenerators.isResolvingWaterfalls()) {
                WaterfallCloudGenerators.addGenerator(Minecraft.getInstance().level, new BlockPos(pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f));
            }
        }
    }
}
