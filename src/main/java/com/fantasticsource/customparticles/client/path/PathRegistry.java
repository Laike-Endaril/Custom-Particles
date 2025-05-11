package com.fantasticsource.customparticles.client.path;

import com.fantasticsource.customparticles.FileWordParser;
import com.fantasticsource.tools.component.path.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

public class PathRegistry extends FileWordParser
{
    //Only allow direct creation of transforming paths; do NOT allow reference to PATHS map from a transformation (avoid circular logic and other complex issues)
    public static final LinkedHashMap<String, CPath> PATHS = new LinkedHashMap<>();

    protected final Stack<CPath> stack = new Stack<>();
    protected CPath mostRecent;


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
                if (currentObject == null) throw new IllegalArgumentException("Failed to load path: " + currentObjectName);
                else if (!PATHS.containsKey(currentObjectName)) PATHS.put(currentObjectName, (CPath) currentObject);
                break;

            case "speed":
            case "rate":
            case "linear":
                if (currentObject != null) throw new IllegalArgumentException("Only one main path can be defined per file!");
                linear(args);
                if (currentObject == null) throw new IllegalArgumentException("Failed to load path: " + currentObjectName);
                else if (!PATHS.containsKey(currentObjectName)) PATHS.put(currentObjectName, (CPath) currentObject);
                break;

            case "sin":
            case "sine":
            case "sinuous":
                if (currentObject != null) throw new IllegalArgumentException("Only one main path can be defined per file!");
                sinuous(args);
                if (currentObject == null) throw new IllegalArgumentException("Failed to load path: " + currentObjectName);
                else if (!PATHS.containsKey(currentObjectName)) PATHS.put(currentObjectName, (CPath) currentObject);
                break;

            case "randompos":
            case "posrandom":
            case "randomposition":
            case "positionrandom":
            case "randomconstant":
            case "constantrandom":
                if (currentObject != null) throw new IllegalArgumentException("Only one main path can be defined per file!");
                randomConstant(args);
                if (currentObject == null) throw new IllegalArgumentException("Failed to load path: " + currentObjectName);
                else if (!PATHS.containsKey(currentObjectName)) PATHS.put(currentObjectName, (CPath) currentObject);
                break;


            case "add":
                if (currentObject == null) throw new IllegalArgumentException("No main path is defined yet!");
                add(args);
                break;

            case "multiply":
            case "mult":
                if (currentObject == null) throw new IllegalArgumentException("No main path is defined yet!");
                mult(args);
                break;

            case "lowlimit":
                if (currentObject == null) throw new IllegalArgumentException("No main path is defined yet!");
                lowLimit(args);
                break;

            case "highlimit":
                if (currentObject == null) throw new IllegalArgumentException("No main path is defined yet!");
                highLimit(args);
                break;


            case "{":
                stack.push((CPath) currentObject);
                currentObject = mostRecent;
                break;

            case "}":
                currentObject = stack.pop();
                mostRecent = (CPath) currentObject;
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
        mostRecent = new CPathConstant(doubles);
        currentObject = mostRecent;
    }

    public void linear(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for path!");


        double[] doubles = new double[args.size()];
        for (int i = 0; i < doubles.length; i++) doubles[i] = Double.parseDouble(args.get(i));
        mostRecent = new CPathLinear(doubles);
        currentObject = mostRecent;
    }

    public void sinuous(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for path!");


        double thetaPerSec = Double.parseDouble(args.remove(0)), thetaOffset = Double.parseDouble(args.remove(0));
        double[] doubles = new double[args.size()];
        for (int i = 0; i < doubles.length; i++) doubles[i] = Double.parseDouble(args.get(i));
        mostRecent = new CPathSinuous(thetaPerSec, thetaOffset, doubles);
        currentObject = mostRecent;
    }

    public void randomConstant(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument for path!");


        double[] doubles = new double[args.size()];
        for (int i = 0; i < doubles.length; i++) doubles[i] = Double.parseDouble(args.get(i));
        mostRecent = new CPathRandomConstant(doubles);
        currentObject = mostRecent;
    }


    public void add(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument!");

        ((CPath) currentObject).add(transformCurrent(args));
    }

    public void mult(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument!");

        ((CPath) currentObject).mult(transformCurrent(args));
    }

    public void lowLimit(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument!");

        ((CPath) currentObject).lowLimit(transformCurrent(args));
    }

    public void highLimit(ArrayList<String> args)
    {
        if (args.size() == 0) throw new IllegalArgumentException("Missing argument!");

        ((CPath) currentObject).highLimit(transformCurrent(args));
    }


    public CPath transformCurrent(ArrayList<String> args)
    {
        CPath oldPath = (CPath) currentObject;
        currentObject = null;

        handleFunction(args.remove(0), args);
        CPath result = (CPath) currentObject;
        currentObject = oldPath;

        return result;
    }
}
