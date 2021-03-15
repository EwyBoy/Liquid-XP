package com.ewyboy.liquidxp.common.loaders;

import com.ewyboy.liquidxp.LiquidXP;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = LiquidXP.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleLoader {

    public static BasicParticleType XP_SPRAY;

    @OnlyIn(Dist.CLIENT)
    public static void initParticles(FMLClientSetupEvent event) {


        //XP_SPRAY = Registry.register(Registry.PARTICLE_TYPE, "spray", XP_SPRAY);
    }

}
