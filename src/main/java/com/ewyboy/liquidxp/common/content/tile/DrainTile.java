package com.ewyboy.liquidxp.common.content.tile;

import com.ewyboy.liquidxp.common.register.Register;
import com.ewyboy.liquidxp.common.util.LevelingUtils;
import com.ewyboy.liquidxp.common.util.XPUtils;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Objects;

public class DrainTile extends TileEntity implements ITickableTileEntity {

    public DrainTile() {
        super(Register.TILES.DRAIN);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            final List<ExperienceOrbEntity> xpOrbsOnGrid = getXPOrbsOnGrid();
            final List<PlayerEntity> playersOnGrid = getPlayersOnGrid();

            if (!xpOrbsOnGrid.isEmpty() || !playersOnGrid.isEmpty()) {
                final BlockPos down = getPos().down();

                if (world.isBlockLoaded(down)) {
                    final TileEntity te = world.getTileEntity(down);

                    if (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).isPresent()) {
                        te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).ifPresent(
                            fluidHandler -> {
                                for (ExperienceOrbEntity orb : xpOrbsOnGrid) {
                                    tryConsumeOrb(fluidHandler, orb);
                                }
                                for (PlayerEntity player : playersOnGrid) {
                                    tryDrainPlayer(fluidHandler, player);
                                }
                            }
                        );
                    }
                }
            }
        }
    }

    protected void tryDrainPlayer(IFluidHandler tank, PlayerEntity player) {
        int playerXP = LevelingUtils.getPlayerXP(player);
        if (playerXP <= 0) return;

        int maxDrainedXp = Math.min(4, playerXP);

        int xpAmount = XPUtils.xpJuiceConverter.xpToFluid(maxDrainedXp);
        FluidStack xpStack = new FluidStack(Register.FLUID.XP, xpAmount);

        int maxAcceptedLiquid = tank.fill(xpStack, IFluidHandler.FluidAction.SIMULATE);

        // rounding down, so we only use as much as we can
        int acceptedXP = XPUtils.xpJuiceConverter.fluidToXp(maxAcceptedLiquid);
        int acceptedLiquid = XPUtils.xpJuiceConverter.xpToFluid(acceptedXP);

        xpStack.setAmount(acceptedLiquid);

        int finallyAcceptedLiquid = tank.fill(xpStack, IFluidHandler.FluidAction.EXECUTE);

        if (finallyAcceptedLiquid <= 0) return;

        if (Objects.requireNonNull(world).getGameTime() % 4 == 0) {
            world.playSound(player, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, 0.5F * ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.8F));
        }

        LevelingUtils.addPlayerXP(player, -acceptedXP);
    }

    protected void tryConsumeOrb(IFluidHandler tank, ExperienceOrbEntity orb) {
        if (orb.isAlive()) {
            int xpAmount = XPUtils.xpJuiceConverter.xpToFluid(orb.getXpValue());
            FluidStack xpStack = new FluidStack(Register.FLUID.XP, xpAmount);
            int filled = tank.fill(xpStack, IFluidHandler.FluidAction.SIMULATE);
            if (filled == xpStack.getAmount()) {
                tank.fill(xpStack, IFluidHandler.FluidAction.EXECUTE);
                orb.remove();
            }
        }
    }

    public static AxisAlignedBB singleBlock(BlockPos pos) {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public static AxisAlignedBB aabbOffset(BlockPos pos, AxisAlignedBB aabb) {
        return aabb.offset(pos.getX(), pos.getY(), pos.getZ());
    }

    protected List<PlayerEntity> getPlayersOnGrid() {
        return world != null ? world.getEntitiesWithinAABB(PlayerEntity.class, singleBlock(pos)) : null;
    }

    protected List<ExperienceOrbEntity> getXPOrbsOnGrid() {
        return world != null ? world.getEntitiesWithinAABB(ExperienceOrbEntity.class, aabbOffset(pos, new AxisAlignedBB(0, 0, 0, 1, 0.3, 1))) : null;
    }

}
