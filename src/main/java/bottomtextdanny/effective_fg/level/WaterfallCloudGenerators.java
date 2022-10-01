package bottomtextdanny.effective_fg.level;

import bottomtextdanny.effective_fg.EffectiveFg;
import bottomtextdanny.effective_fg.registry.ParticleRegistry;
import bottomtextdanny.effective_fg.registry.SoundEventRegistry;
import bottomtextdanny.effective_fg.sound.LinearFadeSound;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public class WaterfallCloudGenerators {
    public static final Set<BlockPos> GENERATORS = new HashSet<>();
    private static boolean resolvingWaterfalls;
    private static Level levelO = null;

    public static void addGenerator(BlockPos blockPos) {
        GENERATORS.add(blockPos);
    }

    public static void tick() {
        if (!EffectiveFg.config().cascades.get()) {
            GENERATORS.clear();
            return;
        }

        Minecraft instance = Minecraft.getInstance();
        ClientLevel level = instance.level;
        Camera camera = instance.getEntityRenderDispatcher().camera;

        if (instance.isPaused() || level == null || camera == null) return;

        BlockPos pos = camera.getBlockPosition();

        if (level != levelO) GENERATORS.clear();

        levelO = level;

        Random random = level.random;

        SoundManager soundManager = instance.getSoundManager();
        float cascadeRange = EffectiveFg.config().cascadeSoundRange.get().floatValue();
        float cascadeRangeSquared = cascadeRange * cascadeRange;

        resolvingWaterfalls = true;
        int maxGeneratorDistance = Mth.square((Math.max(instance.options.renderDistance, instance.options.simulationDistance) + 1) * 16);
        int[] soundCounter = {1};

        GENERATORS.removeIf(blockPos -> {
            int x = pos.getX() - blockPos.getX();
            int z = pos.getZ() - blockPos.getZ();
            int dist = x * x + z * z;
            if (dist >= maxGeneratorDistance) return true;

            BlockState stateUp = level.getBlockState(blockPos.offset(0, 1, 0));
            BlockState stateDoubleUp = level.getBlockState(blockPos.offset(0, 2, 0));
            BlockState state = level.getBlockState(blockPos);

            return !(state.getBlock() == Blocks.WATER
                && state.getBlock() == Blocks.WATER
                && stateUp.getBlock() == Blocks.WATER
                && !stateUp.getFluidState().isSource()
                && stateUp.getFluidState().getOwnHeight() >= EffectiveFg.FLOWING_WATER_SHOULD_BEHAVE_AT_HEIGHT
                && stateDoubleUp.is(Blocks.WATER)
                && !stateDoubleUp.getFluidState().isSource());
        });

        List<BlockPos> generators = GENERATORS.stream().sorted(Comparator.comparingDouble(bp -> bp.distSqr(pos))).collect(Collectors.toList());

        generators.forEach(blockPos -> {
            double dist = blockPos.distSqr(pos);

            double offsetX = random.nextDouble() / 5.0 + 0.2;
            offsetX = random.nextBoolean() ? offsetX : -offsetX;
            double offsetZ = random.nextDouble() / 5.0 + 0.2;
            offsetZ = random.nextBoolean() ? offsetZ : -offsetZ;

            if (((int)level.getGameTime()) % (soundCounter[0] * 7) == 0 && dist < cascadeRangeSquared) {
                soundManager.play(new LinearFadeSound(SoundEventRegistry.AMBIENCE_WATERFALL.get(), SoundSource.AMBIENT, cascadeRange, 1.0F, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            }

            if (random.nextFloat() < EffectiveFg.config().cascadeParticleAmountMultiplier.get())
                level.addParticle(ParticleRegistry.WATERFALL_CLOUD.get(), blockPos.getX() + 0.5 + offsetX, blockPos.getY() + 1.0 + random.nextFloat(), blockPos.getZ() + 0.5 + offsetZ, random.nextFloat() / 5.0 * Math.signum(offsetX), random.nextFloat() / 5.0, random.nextFloat() / 5.0 * Math.signum(offsetZ));

            soundCounter[0] += random.nextInt(2) + 1;
        });

        resolvingWaterfalls = false;
    }

    public static boolean isResolvingWaterfalls() {
        return resolvingWaterfalls;
    }
}
