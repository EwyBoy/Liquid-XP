package com.ewyboy.liquidxp.common.util;

import com.ewyboy.liquidxp.common.config.Config;
import com.ewyboy.liquidxp.common.register.Register;
import com.google.common.collect.Lists;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class XPUtils {

    public static final int XP_PER_BOTTLE = 8;

    public interface IFluidXpConverter {
        public int fluidToXp(int fluid);

        public int xpToFluid(int xp);
    }

    private static class LiquidXP implements IFluidXpConverter {

        @Override
        public int fluidToXp(int fluid) {
            return fluid / Config.SETTINGS.experienceToLiquidRatio.get();
        }

        @Override
        public int xpToFluid(int xp) {
            return xp * Config.SETTINGS.experienceToLiquidRatio.get();
        }

    }

    private static class Linear implements IFluidXpConverter {

        private final int xpToFluid;

        public Linear(int xpToFluid) {
            this.xpToFluid = xpToFluid;
        }

        @Override
        public int fluidToXp(int fluid) {
            return fluid / xpToFluid;
        }

        @Override
        public int xpToFluid(int xp) {
            return xp * xpToFluid;
        }
    }

    private static class ConversionEntry {

        private final FluidStack fluid;
        private final IFluidXpConverter converter;
        private final Optional<IFluidXpConverter> optionalConverter;

        public ConversionEntry(FluidStack fluid, IFluidXpConverter converter) {
            this.fluid = fluid;
            this.converter = converter;
            this.optionalConverter = Optional.of(converter);
        }

        public boolean matches(FluidStack input) {
            return fluid.isFluidEqual(input);
        }

    }

    public static final IFluidXpConverter xpJuiceConverter = new LiquidXP();

    private static final List<ConversionEntry> converters = Lists.newArrayList();

    public static void initializeFromConfig() {
        converters.add(new ConversionEntry(new FluidStack(Register.FLUID.XP, 1000), xpJuiceConverter));

        /*for (String entry : Config.additionalXpFluids) {
            final String[] fields = entry.split(":");
            if (fields.length != 2) {
                Log.warn("Malformed XP fluid entry: %s", entry);
                continue;
            }

            final String fluidName = fields[0];
            final Fluid fluid = FluidRegistry.getFluid(fluidName);

            if (fluid == null) {
                Log.debug("Fluid not found: %s", fluidName);
                continue;
            }

            final int xpToFluid;
            try {
                xpToFluid = Integer.parseInt(fields[1]);
            } catch (NumberFormatException e) {
                Log.warn("Invalid value in XP fluid entry: %s", entry);
                continue;
            }

            converters.add(new ConversionEntry(new FluidStack(fluid, 1000), new Linear(xpToFluid)));
        }*/
    }

    public static FluidStack[] getAcceptedFluids() {
        final FluidStack[] result = new FluidStack[converters.size()];
        int i = 0;
        for (ConversionEntry e : converters) {
            result[i++] = e.fluid.copy();
        }

        return result;
    }

    public static Optional<IFluidXpConverter> getConverter(FluidStack stack) {
        if (stack == null) return Optional.empty();

        for (ConversionEntry e : converters) {
            if (e.matches(stack)) return e.optionalConverter;
        }

        return Optional.empty();
    }

    public static int getMaxPossibleFluidForLevel(int level) {
        final int xp = LevelingUtils.getExperienceForLevel(level);
        return getMaxPossibleFluidForXp(xp);
    }

    public static int getMaxPossibleFluidForXp(final int xp) {
        int result = 0;
        for (ConversionEntry e : converters) {
            result = Math.max(result, e.converter.xpToFluid(xp));
        }
        return result;
    }

    public static final Function<FluidStack, FluidStack> FLUID_TO_LEVELS = input -> {
        if (input == null) return null;
        final Optional<IFluidXpConverter> maybeConverter = getConverter(input);
        return maybeConverter.map(converter -> {
            final FluidStack result = input.copy();
            // display levels instead of actual xp fluid level
            result.setAmount(LevelingUtils.getLevelForExperience(converter.fluidToXp(input.getAmount())));
            return result;
        }).orElse(null);
    };


}
