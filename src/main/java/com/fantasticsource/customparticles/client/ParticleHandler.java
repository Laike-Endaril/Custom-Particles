package com.fantasticsource.customparticles.client;

import com.fantasticsource.customparticles.CustomParticles;
import com.fantasticsource.customparticles.client.emitter.CustomParticleEmitter;
import com.fantasticsource.customparticles.client.emitter.EmitterBlock;
import com.fantasticsource.customparticles.client.emitter.EmitterRegistry;
import com.fantasticsource.customparticles.client.factory.FactoryRegistry;
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
    public static final File
            FACTORIES_DIR = new File(MCTools.getConfigDir() + MODID + File.separator + "factories"),
            EMITTERS_DIR = new File(MCTools.getConfigDir() + MODID + File.separator + "emitters");

    private static final BlockPos.MutableBlockPos MUT_POS = new BlockPos.MutableBlockPos();

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
        if (event.phase != TickEvent.Phase.END || world == null) return;


        if (!initialized)
        {
            //TODO add command to reload settings during runtime, or do it automatically via file name / size detection
            //TODO add filename, line number, line printout for parsing errors

            FACTORIES_DIR.mkdirs();
            new FactoryRegistry().tryLoad(FACTORIES_DIR);

            EMITTERS_DIR.mkdirs();
            new EmitterRegistry().tryLoad(EMITTERS_DIR);

            initialized = true;
        }


        if (!Minecraft.getMinecraft().isGamePaused())
        {
            EntityPlayer player = Minecraft.getMinecraft().player;
            for (Map.Entry<Class<? extends CustomParticleEmitter>, ArrayList<CustomParticleEmitter>> entry : EMITTERS.entrySet())
            {
                if (entry.getKey() == EmitterBlock.class)
                {
                    BlockPos playerPos = player.getPosition();
                    int x = playerPos.getX(), y = playerPos.getY(), z = playerPos.getZ(), eyeY = (int) (player.posY + player.eyeHeight);
                    int xx, yy, zz;
                    Object obj;
                    ArrayList<CustomParticleEmitter> list = new ArrayList<>();
                    CustomParticleEmitter emitter;
                    //Simulate vanilla to some extent, at least for now
                    for (int i = 0; i < 100; i++)
                    {
                        xx = x - 16 + Tools.random(33);
                        yy = eyeY - 16 + Tools.random(33);
                        zz = z - 16 + Tools.random(33);
                        obj = world.getBlockState(MUT_POS.setPos(xx, yy, zz));

                        list.addAll(entry.getValue());
                        while (list.size() > 0)
                        {
                            emitter = Tools.choose(list);
                            if (emitter.clientTick(xx, yy, zz, obj))
                            {
                                list.clear();
                                break;
                            }
                            else list.remove(emitter);
                        }


                        xx = x - 32 + Tools.random(65);
                        yy = eyeY - 32 + Tools.random(65);
                        zz = z - 32 + Tools.random(65);
                        obj = world.getBlockState(MUT_POS.setPos(xx, yy, zz));

                        list.addAll(entry.getValue());
                        while (list.size() > 0)
                        {
                            emitter = Tools.choose(list);
                            if (emitter.clientTick(xx, yy, zz, obj))
                            {
                                list.clear();
                                break;
                            }
                            else list.remove(emitter);
                        }
                    }
                }
            }
        }
    }


    public static void generateInstructionsFile()
    {
        File file = new File(MCTools.getConfigDir() + MODID + File.separator + "INSTRUCTIONS.txt");
        while (!file.exists()) file.mkdirs();
        while (file.exists()) file.delete();


        try
        {
            InputStream in = Tools.getJarResourceStream(CustomParticles.class, "assets/" + MODID + "/INSTRUCTIONS.txt");
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
