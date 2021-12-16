package com.bottomtextdanny.effective_fg.mixin.client;

import com.bottomtextdanny.effective_fg.EffectiveFg;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "animateTick", at = @At("RETURN"))
    protected void animateTick(BlockState state, Level world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.getBlock() == Blocks.WATER && !world.getBlockState(pos.offset(0, 1, 0)).getFluidState().isSource() && world.getBlockState(pos.offset(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(pos.offset(0, 1, 0)).getFluidState().isSource() && world.getBlockState(pos.offset(0, 1, 0)).getFluidState().getOwnHeight() >= EffectiveFg.FLOWING_WATER_SHOULD_BEHAVE_AT_HEIGHT) {
            Vec3 vec3d = state.getFluidState().getFlow(world, pos);
            for (int i = 0; i < random.nextInt(EffectiveFg.FLOWING_WATER_DROP_RARITY); i++) {
                world.addParticle(ParticleTypes.SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.x() * random.nextFloat(), random.nextFloat() / 10f, vec3d.z() * random.nextFloat());
            }
        }
    }
}
