package com.fantasticsource.customparticles;

import com.fantasticsource.tools.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class FileWordParser
{
    protected String currentObjectName;

    public void tryLoad(File file)
    {
        if (!file.exists()) return;


        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files != null) for (File file2 : files) tryLoad(file2);
            return;
        }


        //Actual file parse
        String function;
        ArrayList<String> args = new ArrayList<>();
        try
        {
            currentObjectName = file.getName().replaceFirst("(.*)[.][^.]*", "$1");
            BufferedReader reader = new BufferedReader(new FileReader(file));


            String line = reader.readLine();
            while (line != null)
            {
                function = "";
                args.clear();
                line = line.replaceAll("//.*", "");


                for (String word : Tools.fixedSplit(line, "[ ]"))
                {
                    if (function.equals("")) function = word;
                    else args.add(word);
                }

                handleFunction(function, args);


                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public abstract void handleFunction(String function, ArrayList<String> args);
}
