package com.ewyboy.liquidxp.common.register;

import com.ewyboy.bibliotheca.common.content.fluid.BaseFluid;
import com.ewyboy.bibliotheca.common.loaders.FluidLoader;
import com.ewyboy.liquidxp.LiquidXP;
import com.ewyboy.liquidxp.common.content.block.DrainBlock;
import com.ewyboy.liquidxp.common.content.block.SprinklerBlock;
import com.ewyboy.liquidxp.common.content.tile.DrainTile;
import com.ewyboy.liquidxp.common.content.tile.SprinklerTile;
import com.google.common.collect.Sets;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class Register {

    public static final class BLOCK {
        public static final DrainBlock DRAIN = new DrainBlock(AbstractBlock.Properties.create(Material.ANVIL));
        public static final SprinklerBlock SPRINKLER = new SprinklerBlock(AbstractBlock.Properties.create(Material.ANVIL).notSolid());
    }

    public static final class ITEM {}

    public static final class TILES {
        public static final TileEntityType<DrainTile> DRAIN = new TileEntityType<>(DrainTile :: new, Sets.newHashSet(BLOCK.DRAIN), null);
        public static final TileEntityType<SprinklerTile> SPRINKLER = new TileEntityType<>(SprinklerTile :: new, Sets.newHashSet(BLOCK.SPRINKLER), null);
    }

    public static final class FLUID {

        public static final FlowingFluid XP = new BaseFluid.Source(Properties.xp);
        public static final FlowingFluid XP_FLOW = new BaseFluid.Flowing(Properties.xp);

    }

    public static final class Properties {

        public static final ForgeFlowingFluid.Properties xp = new ForgeFlowingFluid.Properties(
                () -> FLUID.XP,
                () -> FLUID.XP_FLOW,
                FluidAttributes.builder(
                        LiquidXP.prefix("block/fluid/xp_still"),
                        LiquidXP.prefix("block/fluid/xp_flow")
                )
        )
            .block(() -> FluidLoader.fluidBlock)
            .bucket(() -> FluidLoader.bucketItem)
        ;
    }

}
