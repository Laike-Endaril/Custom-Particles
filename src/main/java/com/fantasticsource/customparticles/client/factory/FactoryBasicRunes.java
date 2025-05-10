package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleFactory;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.component.path.CPathRandomConstant;

public class FactoryBasicRunes extends CustomParticleFactory
{
    public PathedParticleFactory factory;


    public FactoryBasicRunes()
    {
        setTexture("minecraft:textures/particle/particles.png");
        spriteMetaData = SpriteMetaData.VANILLA_RUNES_NON_EMPTY;

        factory = args ->
        {
            PathedParticle particle = new PathedParticle(maxAge == -1 ? 60 : maxAge, particleRenderData);
            particle.positionPath(new CPathConstant((double) args[0], (double) args[1], (Double) args[2]));
            particle.animationPath(new CPathRandomConstant(1));
            return particle;
        };
    }


    @Override
    public PathedParticle create(double x, double y, double z)
    {
        return factory.create(x, y, z);
    }
}
