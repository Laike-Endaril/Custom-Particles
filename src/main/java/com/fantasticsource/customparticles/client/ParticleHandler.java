package com.fantasticsource.customparticles.client;

import com.fantasticsource.customparticles.client.factory.LeafFactory;
import com.fantasticsource.tools.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.fantasticsource.customparticles.CustomParticles.MODID;

@SideOnly(Side.CLIENT)
public class ParticleHandler
{
    public static LeafFactory leafFactory = null;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void particleTest(TickEvent.ClientTickEvent event)
    {
        World world = Minecraft.getMinecraft().world;
        if (event.phase != TickEvent.Phase.END || world == null) return;


        if (leafFactory == null)
        {
            leafFactory = new LeafFactory();
            leafFactory.setTexture(MODID + ":textures/particles.png");
        }

        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!Minecraft.getMinecraft().isGamePaused())
        {
            leafFactory.create(player.posX - 3 + Tools.random(6d), player.posY + player.height + 1, player.posZ - 3 + Tools.random(6d));
        }
    }
}
