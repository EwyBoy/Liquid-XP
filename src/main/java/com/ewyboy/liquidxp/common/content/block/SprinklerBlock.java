package com.ewyboy.liquidxp.common.content.block;

import com.ewyboy.bibliotheca.client.interfaces.IHasRenderType;
import com.ewyboy.bibliotheca.common.content.block.BaseTileBlock;
import com.ewyboy.liquidxp.common.content.tile.SprinklerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class SprinklerBlock extends BaseTileBlock implements IHasRenderType {

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    protected static final VoxelShape BED_BASE_SHAPE = Block.makeCuboidShape(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    protected static final VoxelShape CORNER_NW = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D);
    protected static final VoxelShape CORNER_SW = Block.makeCuboidShape(0.0D, 0.0D, 13.0D, 3.0D, 3.0D, 16.0D);
    protected static final VoxelShape CORNER_NE = Block.makeCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 3.0D, 3.0D);
    protected static final VoxelShape CORNER_SE = Block.makeCuboidShape(13.0D, 0.0D, 13.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape NORTH_FACING_SHAPE = VoxelShapes.or(BED_BASE_SHAPE, CORNER_NW, CORNER_NE);
    protected static final VoxelShape SOUTH_FACING_SHAPE = VoxelShapes.or(BED_BASE_SHAPE, CORNER_SW, CORNER_SE);
    protected static final VoxelShape WEST_FACING_SHAPE = VoxelShapes.or(BED_BASE_SHAPE, CORNER_NW, CORNER_SW);
    protected static final VoxelShape EAST_FACING_SHAPE = VoxelShapes.or(BED_BASE_SHAPE, CORNER_NE, CORNER_SE);

    protected static final VoxelShape SHAPE_X = Block.makeCuboidShape(
            4.5D,0.0D,1.0D,
            11.5D,5.0D,15.0D
    );

    protected static final VoxelShape SHAPE_Z = Block.makeCuboidShape(
            1.0D,0.0D,4.5D,
            15.0D,5.0D,11.5D
    );

    private static final AxisAlignedBB SPRINKLER_X_AXIS = new AxisAlignedBB(0.275D, 0.0D, 0.05, 0.725D, 0.3D, 0.95);
    private static final AxisAlignedBB SPRINKLER_Z_AXIS = new AxisAlignedBB(0.05, 0.0D, 0.275D, 0.95, 0.3D, 0.725D);

    public SprinklerBlock(Properties properties) {
        super(properties);
        setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ENABLED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

        Direction direction = state.get(FACING);

        switch(direction) {
            case NORTH:
            case SOUTH:
                return SHAPE_X;
            case WEST:
            default:
                return SHAPE_Z;
        }
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static Direction getSprinklerDirection(IBlockReader reader, BlockPos pos) {
        BlockState blockstate = reader.getBlockState(pos);
        return blockstate.getBlock() instanceof SprinklerBlock ? blockstate.get(FACING) : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (player.getHeldItem(hand).isEmpty()) {
            world.setBlockState(pos, world.getBlockState(pos).get(ENABLED)
                    ? getDefaultState().with(ENABLED, false).with(FACING, world.getBlockState(pos).get(FACING))
                    : getDefaultState().with(ENABLED, true).with(FACING, world.getBlockState(pos).get(FACING))
            );
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ENABLED, FACING);
    }

    @Override
    protected Class getTileClass() {
        return SprinklerTile.class;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.getCutout();
    }

}
