package com.ewyboy.liquidxp.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final ForgeConfigSpec settingSpec;
    public static final Settings SETTINGS;

    static {
        Pair<Config.Settings, ForgeConfigSpec> specPair = (new ForgeConfigSpec.Builder()).configure(Config.Settings::new);
        settingSpec = specPair.getRight();
        SETTINGS = specPair.getLeft();
    }

    public static class Settings {

        public final ForgeConfigSpec.ConfigValue<Integer> experienceToLiquidRatio;

        Settings(ForgeConfigSpec.Builder builder) {
            builder.comment("Liquid XP - Config File").push("SETTINGS");
            this.experienceToLiquidRatio =
                    builder
                            .comment("XP -> Liquid : Conversion Ratio [Max: " + Integer.MAX_VALUE + "mB]")
                            .translation("liquidxp.config.experienceToLiquidRatio")
                            .defineInRange("experienceToLiquidRatio", 20, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }

}
