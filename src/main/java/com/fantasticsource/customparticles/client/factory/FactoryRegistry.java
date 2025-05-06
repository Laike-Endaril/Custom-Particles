package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.customparticles.FileWordParser;

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
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for template function");

        if (args.size() > 1)
        {
            Iterator<String> iterator = args.iterator();
            String error = "Template function only takes one argument!  Arguments given: " + iterator.next();
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
}
