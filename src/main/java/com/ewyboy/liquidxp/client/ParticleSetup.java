package com.ewyboy.liquidxp.client;

import com.ewyboy.liquidxp.LiquidXP;
import com.ewyboy.liquidxp.client.particle.SprayParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ParticleSetup {

    public static void initParticles() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ParticleSetup :: initParticleFactories);
    }

    public static void initParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ModParticleTypes.SPRAY_PARTICLE.get(), SprayParticle.Factory :: new);
    }

    public static class ModParticleTypes {

        public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LiquidXP.MODID);

        public static RegistryObject<BasicParticleType> SPRAY_PARTICLE = register("spray", () -> new BasicParticleType(false));

        private static <T extends ParticleType<?>> RegistryObject<T> register(String name, Supplier<T> sup) {
            return PARTICLES.register(name, sup);
        }

    }

}
