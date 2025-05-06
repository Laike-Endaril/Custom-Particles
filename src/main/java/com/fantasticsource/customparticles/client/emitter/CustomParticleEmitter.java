package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.ParticleHandler;
import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;

import java.util.ArrayList;

public abstract class CustomParticleEmitter
{
    public final ArrayList<CustomParticleFactory> factories = new ArrayList<>();


    public CustomParticleEmitter()
    {
        ParticleHandler.EMITTERS.computeIfAbsent(getClass(), o -> new ArrayList<>()).add(this);
    }


    public abstract boolean clientTick(int x, int y, int z, Object obj);


    public void addFactory(CustomParticleFactory factory)
    {
        factories.add(factory);
    }
}
