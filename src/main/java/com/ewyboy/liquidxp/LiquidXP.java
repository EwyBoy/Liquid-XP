package com.ewyboy.liquidxp;

import com.ewyboy.bibliotheca.common.loaders.ContentLoader;
import com.ewyboy.liquidxp.client.ParticleSetup;
import com.ewyboy.liquidxp.common.config.Config;
import com.ewyboy.liquidxp.common.register.Register;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LiquidXP.MODID)
@Mod.EventBusSubscriber(modid = LiquidXP.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LiquidXP {

    public static final String MODID = "liquidxp";

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MODID, path);
    }

    public LiquidXP() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.settingSpec);
        ContentLoader.init(MODID, itemGroup, Register.BLOCK.class, Register.ITEM.class, Register.TILES.class, Register.FLUID.class);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ParticleSetup :: initParticles);

        registerParticles(modBus);
    }

    private void registerParticles(IEventBus modBus) {
        ParticleSetup.ModParticleTypes.PARTICLES.register(modBus);
    }

    public static final ItemGroup itemGroup = new ItemGroup(LiquidXP.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.ANVIL);
        }
    };




}
