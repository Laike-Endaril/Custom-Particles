package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleFactory;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathAccelerateToTerminalVel;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.component.path.CPathLinear;
import net.minecraft.util.math.Vec3d;

public class LeafFactory extends CustomParticleFactory
{
    public SpriteMetaData spriteMetaData;
    public PathedParticleFactory fallingLeafFactory, groundLeafFactory;
    protected double startingRotationMin, startingRotationMax, spinRate;
    public int leafFadeTicks;
    public CPath
            pathFall = new CPathAccelerateToTerminalVel(1000, 0, 0, 0),
            pathFade = new CPathLinear(0).add(new CPathConstant(2)).highLimit(new CPathConstant(1));


    public LeafFactory()
    {
        useFoliageColor = true;


        spriteMetaData = new SpriteMetaData(128, 128, 0, 0, 8, 8);
        setOnGroundFadeTicks(40);

        setStartingAngle(0, 90);
        setMaxSpinRate(Math.PI * 2);

        setTerminalVelocityMultiplier(1);
        setTerminalVelocityDelayMultiplier(1);


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

            double rotationPercent = Math.random();
            double startingRotation = startingRotationMin + rotationPercent * (startingRotationMax - startingRotationMin);
            particle.rotationPath(new CPathConstant(startingRotation));
            particle.rotationPath(new CPathLinear(2 * (rotationPercent - 0.5) * Tools.random(spinRate)));

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

    public void setMaxSpinRate(double spinRate)
    {
        this.spinRate = spinRate;
    }

    public void setOnGroundFadeTicks(int leafFadeTicks)
    {
        if (leafFadeTicks < 0) leafFadeTicks = 0;
        this.leafFadeTicks = leafFadeTicks;
        ((CPathLinear) pathFade).motionPerSecond.values[0] = -40d / leafFadeTicks;
    }

    public void setTerminalVelocityMultiplier(double multiplier)
    {
        ((CPathAccelerateToTerminalVel) pathFall).terminalVelocity.values[1] = -0.6 * multiplier;
    }

    public void setTerminalVelocityDelayMultiplier(long multiplier)
    {
        ((CPathAccelerateToTerminalVel) pathFall).timeToTerminalVelocity = 1000 * multiplier;
    }


    @Override
    public void create(double x, double y, double z)
    {
        fallingLeafFactory.create(x, y, z);
    }
}
