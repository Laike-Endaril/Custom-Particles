package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.emitter.CustomParticleEmitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class EmitterRegistry
{
    public static final LinkedHashMap<String, CustomParticleEmitter> EMITTERS = new LinkedHashMap<>();


    public static void tryLoad(File file)
    {
        if (!file.exists()) return;


        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files != null) for (File file2 : files) tryLoad(file2);
            return;
        }


        //Actual file parse
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            while (line != null)
            {
                //TODO
                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
