package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleFactory;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.component.path.CPathConstant;

public class FactoryBasic extends CustomParticleFactory
{
    public PathedParticleFactory factory;


    public FactoryBasic()
    {
        setTexture("minecraft:textures/particle/particles.png");
        spriteMetaData = new SpriteMetaData(128, 128, 32, 16, 64, 48);

        factory = args ->
        {
            PathedParticle particle = new PathedParticle(90, particleRenderData);
            particle.positionPath(new CPathConstant((double) args[0], (double) args[1], (Double) args[2]));
            return particle;
        };
    }


    @Override
    public PathedParticle create(double x, double y, double z)
    {
        return factory.create(x, y, z);
    }
}
