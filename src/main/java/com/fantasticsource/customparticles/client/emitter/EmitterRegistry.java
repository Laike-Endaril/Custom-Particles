package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.FileWordParser;
import com.fantasticsource.customparticles.client.factory.FactoryRegistry;
import com.fantasticsource.mctools.blocks.RegistryRegexBlockFilter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class EmitterRegistry extends FileWordParser
{
    public static final LinkedHashMap<String, CustomParticleEmitter> EMITTERS = new LinkedHashMap<>();


    @Override
    public void handleFunction(String function, ArrayList<String> args)
    {
        if (function.equals("template"))
        {
            if (mainObject != null) throw new IllegalArgumentException("Template function can only be called once per file!");
            template(args);
        }
        else
        {
            if (mainObject == null) throw new IllegalArgumentException("Template function must be called before any other function!");
            switch (function)
            {
                case "mode":
                case "modes":
                    modes(args);
                    break;

                case "factory":
                case "factories":
                    factories(args);
                    break;

                case "require":
                case "requires":
                case "requirement":
                case "requirements":
                    requirements(args);
                    break;

                case "dimension":
                case "dimensions":
                    dimensions(args);
                    break;

                case "dimensionsarewhitelist":
                    dimensionsAreWhitelist(args);
                    break;

                case "biome":
                case "biomes":
                    biomes(args);
                    break;

                case "biomesarewhitelist":
                    biomesAreWhitelist(args);
                    break;


                case "":
                    break;

                default:
                    throw new IllegalArgumentException("Invalid function: " + function);
            }
        }
    }


    public void template(ArrayList<String> args)
    {
        if (args.size() < 3) throw new IllegalArgumentException("Not enough arguments for emitter template!");

        if (args.size() > 3)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'template' function only takes 3 arguments!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        switch (args.get(0))
        {
            case "block":
            case "blocks":
                mainObject = new EmitterBlock(RegistryRegexBlockFilter.getInstance(args.get(1)), Boolean.parseBoolean(args.get(2)));
                EMITTERS.put(currentObjectName, (CustomParticleEmitter) mainObject);
                break;

            default:
                throw new IllegalArgumentException("Invalid template name: " + args.get(0));
        }
    }

    public void modes(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter modes!");

        for (String arg : args) ((CustomParticleEmitter) mainObject).addOffsetModes(CustomParticleEmitter.OffsetMode.valueOf(arg.toUpperCase()));
    }

    public void factories(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter factories!");

        for (String arg : args) ((CustomParticleEmitter) mainObject).addFactory(FactoryRegistry.FACTORIES.get(arg));
    }

    public void requirements(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter requirements!");

        for (String arg : args) ((CustomParticleEmitter) mainObject).addRequirements(CustomParticleEmitter.Requirement.valueOf(arg.toUpperCase()));
    }

    public void dimensions(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter dimensions!");

        for (String arg : args) ((CustomParticleEmitter) mainObject).dimensions.add(Integer.parseInt(arg));
    }

    public void dimensionsAreWhitelist(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for dimensionsAreWhitelist!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'dimensionsAreWhitelist' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        ((CustomParticleEmitter) mainObject).dimensionsAreWhitelist = Boolean.parseBoolean(args.get(0));
    }

    public void biomes(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter biomes!");

        for (String arg : args) ((CustomParticleEmitter) mainObject).biomes.add(ForgeRegistries.BIOMES.getValue(new ResourceLocation(arg)));
    }

    public void biomesAreWhitelist(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for biomesAreWhitelist!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'biomesAreWhitelist' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        ((CustomParticleEmitter) mainObject).biomesAreWhitelist = Boolean.parseBoolean(args.get(0));
    }
}
