package com.fantasticsource.customparticles;

import com.fantasticsource.customparticles.client.ParticleHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = CustomParticles.MODID, name = CustomParticles.NAME, version = CustomParticles.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.064,)")
public class CustomParticles
{
    public static final String MODID = "customparticles";
    public static final String NAME = "Custom Particles";
    public static final String VERSION = "1.12.2.002";

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(CustomParticles.class);

        if (event.getSide() == Side.CLIENT) MinecraftForge.EVENT_BUS.register(ParticleHandler.class);
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }
}
