package com.ewyboy.liquidxp.common.content.tile;

import com.ewyboy.liquidxp.client.ParticleSetup;
import com.ewyboy.liquidxp.common.content.block.SprinklerBlock;
import com.ewyboy.liquidxp.common.register.Register;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;
import java.util.Random;

public class SprinklerTile extends TileEntity implements ITickableTileEntity {

    public SprinklerTile() {
        super(Register.TILES.SPRINKLER);
    }

    @Override
    public void tick() {
        if (Objects.requireNonNull(world).getBlockState(pos).get(SprinklerBlock.ENABLED)) {
            if (world.isRemote) {
                sprayParticles();
            } else {
                sprinkle();
            }
        }
    }

    private void sprinkle() {
        if (Objects.requireNonNull(world).getGameTime() % 10 == 0) {
            world.addEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), 10));
        }
    }

    private int ticks;
    private static final double SPRAY_SIDE_SCATTER = Math.toRadians(25);
    private static final double[] SPRINKER_DELTA = new double[] { 0.2, 0.25, 0.5 };
    private static final Random RANDOM = new Random();

    private float getSprayDirection() {
        return MathHelper.sin(ticks * 0.02f);
    }

    private void sprayParticles() {
        ticks++;
        final Direction blockYawRotation = Objects.requireNonNull(world).getBlockState(pos).get(SprinklerBlock.FACING);
        final double nozzleAngle = getSprayDirection();
        final double sprayForwardVelocity = Math.sin(Math.toRadians(nozzleAngle * 25));

        int offsetZ = blockYawRotation.getZOffset();
        int offsetX = blockYawRotation.getXOffset();

        if (blockYawRotation.getAxis() == Direction.Axis.X) {
            offsetX = 1;
        } else {
            offsetZ = 1;
        }

        final double forwardVelocityX = sprayForwardVelocity * offsetZ / -2;
        final double forwardVelocityZ = sprayForwardVelocity * offsetX / 2;

        final double sprinklerDelta = SPRINKER_DELTA[0];
        double outletPosition = -0.5;

        while (outletPosition <= 0.5) {
            final double spraySideVelocity = Math.sin(SPRAY_SIDE_SCATTER * (RANDOM.nextDouble() - 0.5));
            final double sideVelocityX = spraySideVelocity * offsetX;
            final double sideVelocityZ = spraySideVelocity * offsetZ;

            world.addParticle(
                    ParticleSetup.ModParticleTypes.SPRAY_PARTICLE.get(),
                    pos.getX() + 0.5 + (outletPosition * 0.6 * offsetX),
                    pos.getY() + 0.2,
                    pos.getZ() + 0.5 + (outletPosition * 0.6 * offsetZ),
                    (forwardVelocityX + sideVelocityX) / 1.5, 0.45, (forwardVelocityZ + sideVelocityZ) / 1.5
            );
            outletPosition += sprinklerDelta;
        }
    }

}
