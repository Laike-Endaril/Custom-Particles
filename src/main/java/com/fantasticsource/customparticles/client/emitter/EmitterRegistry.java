package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.FileWordParser;
import com.fantasticsource.mctools.blocks.RegistryRegexBlockFilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class EmitterRegistry extends FileWordParser
{
    public static final LinkedHashMap<String, CustomParticleEmitter> EMITTERS = new LinkedHashMap<>();

    protected CustomParticleEmitter currentEmitter = null;


    @Override
    public void handleFunction(String function, ArrayList<String> args)
    {
        switch (function)
        {
            case "template":
                activateTemplate(args);
                break;

            case "":
                break;

            default:
                throw new IllegalArgumentException("Invalid function: " + function);
        }
    }


    public void activateTemplate(ArrayList<String> args)
    {
        if (args.size() < 3) throw new IllegalArgumentException("Not enough arguments for emitter template!");

        switch (args.get(0))
        {
            case "block":
            case "blocks":
                currentEmitter = new EmitterBlock(Boolean.parseBoolean(args.get(1)), RegistryRegexBlockFilter.getInstance(args.get(2)));
                EMITTERS.put(currentObjectName, currentEmitter);
                break;

            default:
                throw new IllegalArgumentException("Invalid template name: " + args.get(0));
        }
    }
}
