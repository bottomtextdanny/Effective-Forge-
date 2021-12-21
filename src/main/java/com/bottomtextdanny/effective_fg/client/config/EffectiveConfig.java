package com.bottomtextdanny.effective_fg.client.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class EffectiveConfig
{
    public static ForgeConfigSpec ConfigSpec;

    public static ConfigValue<Boolean> enableEffects;

    static
    {
        ConfigBuilder builder = new ConfigBuilder("Effective Settings");

        builder.Block("Effects", b -> {
            enableEffects = b.define("Enable All Effects", true);
        });

        ConfigSpec = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }
}
