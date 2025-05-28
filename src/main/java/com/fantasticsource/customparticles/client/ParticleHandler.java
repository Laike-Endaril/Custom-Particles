package com.fantasticsource.customparticles.client;

import com.fantasticsource.customparticles.CustomParticles;
import com.fantasticsource.customparticles.client.emitter.CustomParticleEmitter;
import com.fantasticsource.customparticles.client.emitter.EmitterBlock;
import com.fantasticsource.customparticles.client.emitter.EmitterRegistry;
import com.fantasticsource.customparticles.client.factory.FactoryRegistry;
import com.fantasticsource.customparticles.client.path.PathRegistry;
import com.fantasticsource.mctools.ClientTickTimer;
import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tools.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fantasticsource.customparticles.CustomParticles.MODID;

@SideOnly(Side.CLIENT)
public class ParticleHandler
{
    public static final double SPHERE_DENSITY_MULTIPLIER = 0.0001;

    protected static volatile boolean tickEmitters = false;
    protected static int tickEmitterSkips = 0;


    public static final File
            PATHS_DIR = new File(MCTools.getConfigDir() + MODID + File.separator + "paths"),
            FACTORIES_DIR = new File(MCTools.getConfigDir() + MODID + File.separator + "factories"),
            EMITTERS_DIR = new File(MCTools.getConfigDir() + MODID + File.separator + "emitters"),
            EMITTERS_DISABLED_DIR = new File(MCTools.getConfigDir() + MODID + File.separator + "emittersdisabled");

    public static final LinkedHashMap<Class<? extends CustomParticleEmitter>, ArrayList<CustomParticleEmitter>> EMITTERS = new LinkedHashMap<>();


    public static boolean initialized = false;

    static
    {
        generateInstructionsFile();
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        World world = Minecraft.getMinecraft().world;
        if (event.phase != TickEvent.Phase.START || world == null) return;


        if (!initialized)
        {
            PATHS_DIR.mkdirs();
            new PathRegistry().tryLoad(PATHS_DIR);

            FACTORIES_DIR.mkdirs();
            new FactoryRegistry().tryLoad(FACTORIES_DIR);

            EMITTERS_DIR.mkdirs();
            new EmitterRegistry().tryLoad(EMITTERS_DIR);

            EMITTERS_DISABLED_DIR.mkdirs();

            initialized = true;

            Thread thread = new Thread(ParticleHandler::emitterLogicLoop);
            thread.setName("Custom Particle Emitters");
            thread.start();
        }

        if (tickEmitters && !Minecraft.getMinecraft().isGamePaused()) tickEmitterSkips++;
        if (ClientTickTimer.currentTick() % 20 == 0)
        {
            if (tickEmitterSkips > 0) System.err.println("Tick-based particle emitters skipped " + tickEmitterSkips + " tick(s) in the last second");
            tickEmitterSkips = 0;
        }
        tickEmitters = true;
    }


    public static void generateInstructionsFile()
    {
        File file;
        for (String filename : new String[]{"TUTORIAL.txt", "Specifications.txt", "Advanced.txt"})
        {
            file = new File(MCTools.getConfigDir() + MODID + File.separator + filename);
            while (!file.exists()) file.mkdirs();
            while (file.exists()) file.delete();


            try
            {
                InputStream in = Tools.getJarResourceStream(CustomParticles.class, "assets/" + MODID + "/" + filename);
                FileOutputStream out = new FileOutputStream(file);

                byte[] buf = new byte[8192];
                int length;
                while ((length = in.read(buf)) != -1) out.write(buf, 0, length);

                in.close();
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    protected static void emitterLogicLoop()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        World world;
        EntityPlayer player;
        BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
        double[] pos;
        double x, y, z;
        Object obj;
        while (true)
        {
            if (tickEmitters && !minecraft.isGamePaused())
            {
                tickEmitters = false;


                world = minecraft.world;
                player = minecraft.player;
                if (world != null && player != null)
                {
                    x = player.posX;
                    y = (int) (player.posY + player.eyeHeight);
                    z = player.posZ;

                    for (Map.Entry<Class<? extends CustomParticleEmitter>, ArrayList<CustomParticleEmitter>> entry : EMITTERS.entrySet())
                    {
                        //All emitters of a specific type (see CustomParticleEmitter.getType())
                        if (entry.getKey() == EmitterBlock.class)
                        {
                            for (CustomParticleEmitter emitter : entry.getValue())
                            {
                                //All block emitters
                                for (int i = (int) (emitter.density * emitter.getMaxSpawnDistanceCubed() * SPHERE_DENSITY_MULTIPLIER); i >= 0; i--)
                                {
                                    pos = Tools.randomWithinSphere(emitter.getMaxSpawnDistance());
                                    obj = world.getBlockState(mutPos.setPos(x + pos[0], y + pos[1], z + pos[2]));
                                    emitter.triggerFactories(mutPos.getX(), mutPos.getY(), mutPos.getZ(), obj);
                                }
                            }
                        }
                    }
                }
            }

            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
