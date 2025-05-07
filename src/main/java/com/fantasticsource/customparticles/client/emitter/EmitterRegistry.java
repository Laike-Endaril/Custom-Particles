package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.FileWordParser;
import com.fantasticsource.customparticles.client.factory.FactoryRegistry;
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
                template(args);
                break;

            case "mode":
            case "modes":
                modes(args);
                break;

            case "factory":
            case "factories":
                factories(args);
                break;

            case "":
                break;

            default:
                throw new IllegalArgumentException("Invalid function: " + function);
        }
    }


    public void template(ArrayList<String> args)
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


    public void modes(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter modes!");

        for (String arg : args)
        {
            currentEmitter.addOffsetModes(CustomParticleEmitter.OffsetMode.valueOf(arg.toUpperCase()));
        }
    }


    public void factories(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter factories!");

        for (String arg : args)
        {
            currentEmitter.addFactory(FactoryRegistry.FACTORIES.get(arg));
        }
    }
}
