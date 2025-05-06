package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.FileWordParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class EmitterRegistry extends FileWordParser
{
    public static final LinkedHashMap<String, CustomParticleEmitter> EMITTERS = new LinkedHashMap<>();

    protected CustomParticleEmitter currentEmitter = null;


    @Override
    public void handleFunction(String function, ArrayList<String> args)
    {
        //TODO
    }
}
