package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleFactory;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathAccelerative;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.component.path.CPathLinear;
import net.minecraft.util.math.Vec3d;

public class LeafFactory extends CustomParticleFactory
{
    public SpriteMetaData spriteMetaData;
    public PathedParticleFactory fallingLeafFactory, groundLeafFactory;
    protected double startingRotationMin, startingRotationMax, spinMultiplier = 1;
    public int leafFadeTicks = 0;
    public CPath
            pathTerminalVelocity = new CPathLinear(0, 0, 0),
            pathFall = new CPathAccelerative(0, 0, 0).lowLimit(pathTerminalVelocity),
            pathFade = new CPathLinear(0).add(new CPathConstant(2)).highLimit(new CPathConstant(1));


    public LeafFactory()
    {
        useFoliageColor = true;


        spriteMetaData = new SpriteMetaData(128, 128, 0, 0, 8, 8);
        setStartingAngle(0, 90);
        setOnGroundFadeTicks(40);
        setGravityMultiplier(1);
        setTerminalVelocityMultiplier(1);


        groundLeafFactory = args ->
        {
            PathedParticle particle = new PathedParticle(leafFadeTicks, particleRenderData);

            PathedParticle parent = (PathedParticle) args[0];
            Vec3d deathPos = parent.deathPos;
            double y = parent.deathPos.y;
            if (parent.getAge() != parent.maxAge) y += 0.01;
            particle.positionPath(new CPathConstant(deathPos.x, y, deathPos.z));

            particle.spriteMetaData = spriteMetaData;
            particle.useFoliageColor = useFoliageColor;

            particle.rotationPath(new CPathConstant(parent.rotationData.getRelativePosition(parent.currentRenderMillis(0))));

            particle.alphaPath(pathFade);

            return particle;
        };


        fallingLeafFactory = args ->
        {
            PathedParticle particle = new PathedParticle(200, particleRenderData);

            particle.positionPath(new CPathConstant((double) args[0], (double) args[1], (Double) args[2]));
            particle.positionPath(pathFall);

            particle.spriteMetaData = spriteMetaData;
            particle.useFoliageColor = useFoliageColor;

            particle.rotationPath(new CPathConstant(startingRotationMin + Tools.random(startingRotationMax - startingRotationMin)));

            particle.dieOnSolidsAndLiquids();

            particle.addOnDeathParticles(groundLeafFactory);

            return particle;
        };
    }


    public void setStartingAngle(double minDegrees, double maxDegrees)
    {
        startingRotationMin = -Tools.degtorad(minDegrees);
        startingRotationMax = -Tools.degtorad(maxDegrees);
    }

    public void setSpinRates(double minDegreesPerSecond, double maxDegreesPerSecond)
    {

    }

    public void setOnGroundFadeTicks(int leafFadeTicks)
    {
        if (leafFadeTicks < 0) leafFadeTicks = 0;
        this.leafFadeTicks = leafFadeTicks;
        ((CPathLinear) pathFade).motionPerSecond.values[0] = -40d / leafFadeTicks;
    }

    public void setGravityMultiplier(double multiplier)
    {
        ((CPathAccelerative) pathFall).motionPerSecondPerSecond.values[1] = -0.8 * Math.abs(multiplier);
    }

    public void setTerminalVelocityMultiplier(double multiplier)
    {
        ((CPathLinear) pathTerminalVelocity).motionPerSecond.values[1] = -0.8 * Math.abs(multiplier);
    }


    @Override
    public void create(double x, double y, double z)
    {
        fallingLeafFactory.create(x, y, z);
    }
}
