package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.tools.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class FactoryRegistry
{
    public static final LinkedHashMap<String, CustomParticleFactory> FACTORIES = new LinkedHashMap<>();

    protected static String currentFactoryName = "";
    protected static CustomParticleFactory currentFactory = null;


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
        String function = "";
        ArrayList<String> args = new ArrayList<>();
        try
        {
            currentFactoryName = file.getName().replaceFirst("(.*)[.][^.]*", "$1");
            BufferedReader reader = new BufferedReader(new FileReader(file));


            String line = reader.readLine();
            while (line != null)
            {
                function = "";
                args.clear();
                line = line.replaceAll("//.*", "");


                for (String word : Tools.fixedSplit(line, "[ ,:;]"))
                {
                    if (function.equals("")) function = word;
                    else args.add(word);
                }


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


                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void activateTemplate(ArrayList<String> args)
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
                FACTORIES.put(currentFactoryName, currentFactory);
                System.out.println(currentFactoryName + " " + args.get(0));
                break;

            default:
                throw new IllegalArgumentException("Invalid template name: " + args.get(0));
        }
    }
}
