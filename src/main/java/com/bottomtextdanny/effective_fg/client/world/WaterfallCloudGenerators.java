package com.bottomtextdanny.effective_fg.client.world;

import com.bottomtextdanny.effective_fg.EffectiveFg;
import com.bottomtextdanny.effective_fg.client.config.EffectiveConfig;
import com.bottomtextdanny.effective_fg.registry.ParticleRegistry;
import com.bottomtextdanny.effective_fg.registry.SoundEventRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WaterfallCloudGenerators {
    public static final Set<WaterfallCloudGenerator> generators = Sets.newHashSet();
    private static boolean resolvingWaterfalls;

    public static void tryAddGenerator(IBlockDisplayReader world, BlockPos pos) {
        if (!EffectiveConfig.enableEffects.get())
            return;

        final BlockPos abovePos = pos.above();

        if (world.getBlockState(pos).getBlock() == Blocks.WATER &&
                world.getBlockState(pos).getFluidState().isSource() &&
                world.getBlockState(abovePos).getBlock() == Blocks.WATER &&
                !world.getBlockState(abovePos).getFluidState().isSource() &&
                world.getBlockState(abovePos).getFluidState().getOwnHeight() >= 0.77f) {
            assert Minecraft.getInstance().level != null;
            addGenerator(Minecraft.getInstance().level, new BlockPos(pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f));
        }
    }

    public static void addGenerator(World level, BlockPos blockPos) {
        generators.add(new WaterfallCloudGenerator(level, blockPos));
    }

    public static void removeGenerator(World level, BlockPos blockPos) {
        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.level == level && waterfallCloudGenerator.blockPos == blockPos);
    }

    public static void tick() {
        resolvingWaterfalls = true;
        List<WaterfallCloudGenerator> generatorsToRemove = Lists.newLinkedList();
        List<WaterfallCloudGenerator> generatorsInDistance = generators
                .stream()
                .filter(waterfallCloudGenerator -> waterfallCloudGenerator.level == Minecraft.getInstance().level &&
                        Math.sqrt(waterfallCloudGenerator.blockPos.distSqr(Minecraft.getInstance().player.blockPosition())) <= Minecraft.getInstance().options.renderDistance * 8f).collect(Collectors.toList());

        int counter = 0;
        for (WaterfallCloudGenerator waterfallCloudGenerator: generatorsInDistance) {
            World level = waterfallCloudGenerator.level;
            BlockPos blockPos = waterfallCloudGenerator.blockPos;

            BlockState stateUp = level.getBlockState(blockPos.offset(0, 1, 0));
            BlockState stateDoubleUp = level.getBlockState(blockPos.offset(0, 2, 0));
            if (!(level.getBlockState(blockPos).getBlock() == Blocks.WATER && level.getBlockState(blockPos).getFluidState().isSource() && stateUp.getBlock() == Blocks.WATER && !stateUp.getFluidState().isSource() && stateUp.getFluidState().getOwnHeight() >= EffectiveFg.FLOWING_WATER_SHOULD_BEHAVE_AT_HEIGHT && stateDoubleUp.is(Blocks.WATER) && !stateDoubleUp.getFluidState().isSource())) {
                generatorsToRemove.add(waterfallCloudGenerator);
                continue;
            }

            if (level.random.nextInt(EffectiveFg.WATERFALL_PARTICLE_RARITY) == 0 && level.getBlockState(blockPos).getBlock() == Blocks.WATER && level.getBlockState(blockPos).getFluidState().isSource() && level.getBlockState(blockPos.offset(0, 1, 0)).getBlock() == Blocks.WATER && !level.getBlockState(blockPos.offset(0, 1, 0)).getFluidState().isSource() && level.getBlockState(blockPos.offset(0, 1, 0)).getFluidState().getOwnHeight() >= EffectiveFg.FLOWING_WATER_SHOULD_BEHAVE_AT_HEIGHT) {
                double offsetX = level.random.nextDouble() / 5f + 0.2;
                offsetX = level.random.nextBoolean() ? offsetX : -offsetX;
                double offsetZ = level.random.nextDouble() / 5f + 0.2;
                offsetZ = level.random.nextBoolean() ? offsetZ : -offsetZ;

                if (counter < 50 && level.random.nextInt(EffectiveFg.WATERFALL_PARTICLE_SOUND_RARITY) == 0) {
                    level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEventRegistry.AMBIENCE_WATERFALL.get(), SoundCategory.AMBIENT, 1.5f, 1.2f + level.random.nextFloat() / 10f, true);
                }
                level.addParticle(ParticleRegistry.WATERFALL_CLOUD.get(), blockPos.getX() + 0.5 + offsetX, blockPos.getY() + 1 + level.random.nextFloat(), blockPos.getZ() + 0.5 + offsetZ, level.random.nextFloat() / 5f * Math.signum(offsetX), level.random.nextFloat() / 5f, level.random.nextFloat() / 5f * Math.signum(offsetZ));
            }
            counter++;
        }

        generatorsToRemove.forEach(generators::remove);
        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.level != Minecraft.getInstance().player.level || Math.sqrt(waterfallCloudGenerator.blockPos.distSqr(Minecraft.getInstance().player.blockPosition())) >= Minecraft.getInstance().options.renderDistance * 16f);

        resolvingWaterfalls = false;
    }

    public static boolean isResolvingWaterfalls() {
        return resolvingWaterfalls;
    }

    public static class WaterfallCloudGenerator {
        public World level;
        public BlockPos blockPos;

        public WaterfallCloudGenerator(World level, BlockPos blockPos) {
            this.level = level;
            this.blockPos = blockPos;
        }
    }
}
