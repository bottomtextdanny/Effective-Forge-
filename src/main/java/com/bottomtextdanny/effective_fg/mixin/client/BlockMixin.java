package com.bottomtextdanny.effective_fg.mixin.client;

import com.bottomtextdanny.effective_fg.EffectiveFg;
import com.bottomtextdanny.effective_fg.client.config.EffectiveConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "animateTick", at = @At("RETURN"))
    protected void animateTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (EffectiveConfig.enableEffects.get() && state.getBlock() == Blocks.WATER && !world.getBlockState(pos.offset(0, 1, 0)).getFluidState().isSource() && world.getBlockState(pos.offset(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(pos.offset(0, 1, 0)).getFluidState().isSource() && world.getBlockState(pos.offset(0, 1, 0)).getFluidState().getOwnHeight() >= EffectiveFg.FLOWING_WATER_SHOULD_BEHAVE_AT_HEIGHT) {
            Vector3d vec3d = state.getFluidState().getFlow(world, pos);
            for (int i = 0; i < random.nextInt(EffectiveFg.FLOWING_WATER_DROP_RARITY); i++) {
                world.addParticle(ParticleTypes.SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.x() * random.nextFloat(), random.nextFloat() / 10f, vec3d.z() * random.nextFloat());
            }
        }
    }
}
