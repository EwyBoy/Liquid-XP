package com.ewyboy.liquidxp.common.content.block;

import com.ewyboy.bibliotheca.client.interfaces.IHasRenderType;
import com.ewyboy.bibliotheca.common.content.block.BaseTileBlock;
import com.ewyboy.liquidxp.common.content.tile.DrainTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class DrainBlock extends BaseTileBlock<DrainTile> implements IHasRenderType {

    public DrainBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected Class<DrainTile> getTileClass() {
        return DrainTile.class;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(
                2.0D, 0.0D, 2.0D,
                14.0D, 1.0D, 14.0D
        );
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.getCutout();
    }

}
