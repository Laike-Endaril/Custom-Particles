package com.fantasticsource.customparticles.client.path;

import com.fantasticsource.customparticles.FileWordParser;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.component.path.CPathLinear;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

public class PathRegistry extends FileWordParser
{
    public static final LinkedHashMap<String, CPath> PATHS = new LinkedHashMap<>();
    public static final Stack<CPath> PATH_STACK = new Stack<>();


    @Override
    public void handleFunction(String function, ArrayList<String> args)
    {
        switch (function)
        {
            case "pos":
            case "position":
            case "constant":
                if (currentObject != null) throw new IllegalArgumentException("Only one main path can be defined per file!");
                constant(args);
                break;

            case "speed":
            case "rate":
            case "linear":
                if (currentObject != null) throw new IllegalArgumentException("Only one main path can be defined per file!");
                linear(args);
                break;


            case "":
                break;

            default:
                throw new IllegalArgumentException("Invalid function: " + function);
        }
    }


    public void constant(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for path!");


        double[] doubles = new double[args.size()];
        for (int i = 0; i < doubles.length; i++) doubles[i] = Double.parseDouble(args.get(i));
        currentObject = new CPathConstant(doubles);
        PATHS.put(currentObjectName, (CPath) currentObject);
        PATH_STACK.push((CPath) currentObject);
    }

    public void linear(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for path!");


        double[] doubles = new double[args.size()];
        for (int i = 0; i < doubles.length; i++) doubles[i] = Double.parseDouble(args.get(i));
        currentObject = new CPathLinear(doubles);
        PATHS.put(currentObjectName, (CPath) currentObject);
        PATH_STACK.push((CPath) currentObject);
    }
}
