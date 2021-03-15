package com.ewyboy.liquidxp.client.particle;

import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class SprayParticle extends SpriteTexturedParticle {

    private Random random = new Random();

    public SprayParticle(ClientWorld world, IAnimatedSprite sprite, double x, double y, double z, float scale, float gravity, Vec3d velocity) {
        super(world, x, y, z, velocity.x, velocity.y, velocity.z);
        this.setSprite(sprite.get(random));

        particleGravity = gravity;
        maxAge = 50;
        setSize(0.2f, 0.2f);
        multiplyParticleScaleBy(scale);
        canCollide = true;
        this.motionX = velocity.x;
        this.motionY = velocity.y;
        this.motionZ = velocity.z;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }


    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SprayParticle sprayParticle = new SprayParticle(world, this.spriteSet, x, y, z, 1.0f, 1.0f, new Vec3d(xSpeed, ySpeed, zSpeed));

            return sprayParticle;
        }
    }


}
