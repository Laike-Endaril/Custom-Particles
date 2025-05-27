package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.FileWordParser;
import com.fantasticsource.customparticles.client.factory.FactoryRegistry;
import com.fantasticsource.mctools.blocks.AdvancedBlockFilter;
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
            if (currentObject != null) throw new IllegalArgumentException("Template function can only be called once per file!");
            template(args);
        }
        else if (function.equals(""))
        {
        }
        else
        {
            if (currentObject == null) throw new IllegalArgumentException("Template function must be called before any other function!");
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
        if (args.size() < 2) throw new IllegalArgumentException("Not enough arguments for emitter template!");

        switch (args.get(0))
        {
            case "block":
            case "blocks":
                if (args.size() > 3)
                {
                    Iterator<String> iterator = args.iterator();
                    iterator.next();
                    String error = "'template " + args.get(0) + "' only takes 2 additional arguments!  Arguments given: " + iterator.next();
                    while (iterator.hasNext()) error += ", " + iterator.next();
                    throw new IllegalArgumentException(error);
                }

                currentObject = new EmitterBlock(AdvancedBlockFilter.getInstance(args.get(1)), Boolean.parseBoolean(args.get(2)));
                EMITTERS.put(currentObjectName, (CustomParticleEmitter) currentObject);
                break;

            case "wallfall":
            case "wallfaller":
            case "falldownwall":
            case "wallrain":
                if (args.size() > 6)
                {
                    Iterator<String> iterator = args.iterator();
                    iterator.next();
                    String error = "'template " + args.get(0) + "' only takes 5 additional arguments!  Arguments given: " + iterator.next();
                    while (iterator.hasNext()) error += ", " + iterator.next();
                    throw new IllegalArgumentException(error);
                }

                currentObject = new EmitterWallFall(AdvancedBlockFilter.getInstance(args.get(1)), Boolean.parseBoolean(args.get(2)), Integer.parseInt(args.get(3)), Integer.parseInt(args.get(4)), Double.parseDouble(args.get(5)));
                EMITTERS.put(currentObjectName, (CustomParticleEmitter) currentObject);
                break;


            default:
                throw new IllegalArgumentException("Invalid template name: " + args.get(0));
        }
    }

    public void modes(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter modes!");

        for (String arg : args) ((CustomParticleEmitter) currentObject).addOffsetModes(CustomParticleEmitter.OffsetMode.valueOf(arg.toUpperCase()));
    }

    public void factories(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter factories!");

        for (String arg : args) ((CustomParticleEmitter) currentObject).addFactory(FactoryRegistry.FACTORIES.get(arg));
    }

    public void requirements(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter requirements!");

        for (String arg : args) ((CustomParticleEmitter) currentObject).addRequirements(CustomParticleEmitter.Requirement.valueOf(arg.toUpperCase()));
    }

    public void dimensions(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter dimensions!");

        for (String arg : args) ((CustomParticleEmitter) currentObject).dimensions.add(Integer.parseInt(arg));
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

        ((CustomParticleEmitter) currentObject).dimensionsAreWhitelist = Boolean.parseBoolean(args.get(0));
    }

    public void biomes(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("No arguments specified for emitter biomes!");

        for (String arg : args) ((CustomParticleEmitter) currentObject).biomes.add(ForgeRegistries.BIOMES.getValue(new ResourceLocation(arg)));
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

        ((CustomParticleEmitter) currentObject).biomesAreWhitelist = Boolean.parseBoolean(args.get(0));
    }
}
