package bottomtextdanny.effective_fg.mixin;

import bottomtextdanny.effective_fg.level.WaterfallCloudGenerators;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
    public void renderFluid(BlockPos pos, BlockAndTintGetter world, VertexConsumer vertexConsumer, BlockState blockState, FluidState state, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        BlockState stateDoubleUp = world.getBlockState(pos.offset(0, 2, 0));
        BlockState stateUp = world.getBlockState(pos.offset(0, 1, 0));
        if (blockState.getBlock() == Blocks.WATER
            && state.isSource()
            && stateUp.getBlock() == Blocks.WATER
            && !stateUp.getFluidState().isSource()
            && stateUp.getFluidState().getOwnHeight() >= 0.77f
            && stateDoubleUp.is(Blocks.WATER) && !stateDoubleUp.getFluidState().isSource()) {
            if (!WaterfallCloudGenerators.isResolvingWaterfalls()) {
                WaterfallCloudGenerators.addGenerator(new BlockPos(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
            }
        }
    }
}
