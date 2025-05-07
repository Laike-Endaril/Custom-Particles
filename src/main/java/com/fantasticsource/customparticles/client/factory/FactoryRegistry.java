package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.customparticles.FileWordParser;
import com.fantasticsource.tools.SpriteMetaData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class FactoryRegistry extends FileWordParser
{
    public static final LinkedHashMap<String, CustomParticleFactory> FACTORIES = new LinkedHashMap<>();

    protected CustomParticleFactory currentFactory = null;


    @Override
    public void handleFunction(String function, ArrayList<String> args)
    {
        switch (function.toLowerCase())
        {
            case "template":
                template(args);
                break;

            case "usefoliagecolor":
                useFoliageColor(args);
                break;

            case "startingangle":
                startingAngle(args);
                break;

            case "spinrate":
                spinRate(args);
                break;

            case "terminalvelocitymultiplier":
            case "terminalvelocitymult":
                terminalVelocityMult(args);
                break;

            case "terminalvelocitydelaymultiplier":
            case "terminalvelocitydelaymult":
                terminalVelocityDelayMult(args);
                break;

            case "useblocklight":
                useBlockLight(args);
                break;

            case "blendsrcfactor":
            case "blendsourcefactor":
                blendSourceFactor(args);
                break;

            case "blenddstfactor":
            case "blenddestinationfactor":
                blendDestinationFactor(args);
                break;

            case "tex":
            case "texture":
                texture(args);
                break;

            case "culldistance":
            case "cullingdistance":
                cullingDistance(args);
                break;

            case "spritemeta":
            case "spritemetadata":
                spriteMetaData(args);
                break;


            case "":
                break;

            default:
                throw new IllegalArgumentException("Invalid function: " + function);
        }
    }


    public void template(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for factory template!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'template' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        switch (args.get(0))
        {
            case "leaf":
            case "leaves":
                currentFactory = new FactoryLeaf();
                FACTORIES.put(currentObjectName, currentFactory);
                break;

            default:
                throw new IllegalArgumentException("Invalid template name: " + args.get(0));
        }
    }

    public void useFoliageColor(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for useFoliageColor!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'useFoliageColor' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        currentFactory.useFoliageColor = Boolean.parseBoolean(args.get(0));
    }

    public void startingAngle(ArrayList<String> args)
    {
        if (args.size() > 2)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'startingAngle' function only takes 1 or 2 arguments!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        if (args.size() == 1) currentFactory.setStartingAngle(Double.parseDouble(args.get(0)), Double.parseDouble(args.get(0)));
        else currentFactory.setStartingAngle(Double.parseDouble(args.get(0)), Double.parseDouble(args.get(1)));
    }

    public void spinRate(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for spinRate!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'spinRate' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        currentFactory.setMaxSpinRate(Double.parseDouble(args.get(0)));
    }

    public void terminalVelocityMult(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for terminalVelocityMult!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'terminalVelocityMult' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        ((FactoryLeaf) currentFactory).setTerminalVelocityMultiplier(Double.parseDouble(args.get(0)));
    }

    public void terminalVelocityDelayMult(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for terminalVelocityDelayMult!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'terminalVelocityDelayMult' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        ((FactoryLeaf) currentFactory).setTerminalVelocityDelayMultiplier(Double.parseDouble(args.get(0)));
    }

    public void useBlockLight(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for useBlockLight!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'useBlockLight' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        currentFactory.useBlockLight(Boolean.parseBoolean(args.get(0)));
    }

    public void blendSourceFactor(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for blendSourceFactor!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'blendSourceFactor' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        currentFactory.setBlendSourceFactor(args.get(0));
    }

    public void blendDestinationFactor(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for blendDestinationFactor!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'blendDestinationFactor' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        currentFactory.setBlendDestinationFactor(args.get(0));
    }

    public void texture(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for texture!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'texture' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        currentFactory.setTexture(args.get(0));
    }

    public void cullingDistance(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for cullingDistance!");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "'cullingDistance' function only takes 1 argument!  Arguments given: " + iterator.next();
            while (iterator.hasNext()) error += ", " + iterator.next();
            throw new IllegalArgumentException(error);
        }

        currentFactory.setCullingDistance(Integer.parseInt(args.get(0)));
    }

    public void spriteMetaData(ArrayList<String> args)
    {
        if (args.size() < 6) throw new IllegalArgumentException("Not enough arguments for spriteMetaData!");

        try
        {
            int[] pxVals = new int[args.size() - 2];
            for (int i = 0; i < pxVals.length; i++) pxVals[i] = Integer.parseInt(args.get(i + 2));
            currentFactory.spriteMetaData = new SpriteMetaData(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)), pxVals);
        }
        catch (NumberFormatException e)
        {
            currentFactory.spriteMetaData = new SpriteMetaData(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)), Integer.parseInt(args.get(3)), Integer.parseInt(args.get(4)), Integer.parseInt(args.get(5)), Boolean.parseBoolean(args.get(6)), Integer.parseInt(args.get(7)));
        }
    }
}
