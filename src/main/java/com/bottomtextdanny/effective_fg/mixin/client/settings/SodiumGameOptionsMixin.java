package com.bottomtextdanny.effective_fg.mixin.client.settings;

import com.bottomtextdanny.effective_fg.client.config.EffectiveConfig;
import com.bottomtextdanny.effective_fg.client.world.WaterfallCloudGenerators;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(SodiumGameOptionPages.class)
public class SodiumGameOptionsMixin
{
    @Shadow
    @Final
    private static SodiumOptionsStorage sodiumOpts;

    @Inject(
            method = "experimental",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false,
            cancellable = true
    )
    private static void Inject(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups)
    {
        OptionImpl<SodiumGameOptions, Boolean> enableEffects = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("Enable Water Effects")
                .setTooltip("Toggles off 'Effective' Water and Lava Effects")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EffectiveConfig.enableEffects.set(value);
                            if (!value)
                            {
                                WaterfallCloudGenerators.generators.clear();
                            }
                        },
                        (options) -> EffectiveConfig.enableEffects.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(enableEffects)
                .build()
        );
    }
}