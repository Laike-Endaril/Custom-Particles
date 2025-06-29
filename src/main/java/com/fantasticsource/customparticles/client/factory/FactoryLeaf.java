package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleFactory;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathAccelerateToTerminalVel;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.component.path.CPathLinear;
import com.fantasticsource.tools.datastructures.VectorN;
import net.minecraft.util.math.Vec3d;

import static com.fantasticsource.customparticles.CustomParticles.MODID;

public class FactoryLeaf extends CustomParticleFactory
{
    public PathedParticleFactory fallingLeafFactory, groundLeafFactory;
    public int leafFadeTicks;
    public CPath
            pathFall = new CPathAccelerateToTerminalVel(1000, 0, 0, 0),
            pathFadeInternal = new CPathLinear(0),
            pathFade = new CPathConstant(0).add(pathFadeInternal).highLimit(new CPathConstant(1));


    public FactoryLeaf()
    {
        maxAge = 200;

        useBlockColor = true;
        useBlockLight(true);

        setTexture(MODID + ":textures/particles.png");

        spriteMetaData = new SpriteMetaData(128, 128, 0, 0, 8, 8);
        setOnGroundFadeTicks(40);

        setStartingAngle(45 - 30, 45 + 30);
        setMaxSpinRate(180);

        setTerminalVelocityMultiplier(1);
        setTerminalVelocityDelayMultiplier(1);


        groundLeafFactory = args ->
        {
            PathedParticle parent = (PathedParticle) args[0];
            if (parent.age >= parent.maxAge) return null;


            PathedParticle particle = new PathedParticle(maxAge, particleRenderData);
            particle.age = Tools.max(parent.age, maxAge - leafFadeTicks * 2);

            particle.cullDistanceSquared = parent.cullDistanceSquared;

            Vec3d deathPos = parent.deathPos;
            double x = deathPos.x, y = deathPos.y, z = deathPos.z;
            if (parent.age != parent.maxAge)
            {
                VectorN prevPos = parent.age > 0 ? parent.prevPosition(0) : parent.currentPos(0);
                if (prevPos.values[0] < x) x -= 0.01;
                else if (prevPos.values[0] > x) x += 0.01;
                if (prevPos.values[1] < y) y -= 0.01;
                else if (prevPos.values[1] > y) y += 0.01;
                if (prevPos.values[2] < z) z -= 0.01;
                else if (prevPos.values[2] > z) z += 0.01;
            }
            particle.positionPath(new CPathConstant(x, y, z));

            particle.spriteMetaData = spriteMetaData;

            particle.rotationPath(new CPathConstant(parent.rotationData.getRelativePosition(parent.currentRenderMillis(0))));
            if (parent.rgbData != null) particle.rgbPath(new CPathConstant(parent.rgbData.getRelativePosition(parent.currentRenderMillis(0))));

            particle.alphaPath(pathFade);

            return particle;
        };


        fallingLeafFactory = args ->
        {
            PathedParticle particle = new PathedParticle(maxAge, particleRenderData);
            double x = (double) args[0], y = (double) args[1], z = (double) args[2];
            particle.positionPath(new CPathConstant(x, y, z));
            particle.positionPath(pathFall);

            double rotationPercent = Math.random();
            double startingRotation = startingRotationMin + rotationPercent * (startingRotationMax - startingRotationMin);
            particle.rotationPath(new CPathConstant(startingRotation));
            particle.rotationPath(new CPathLinear(-spinRate * 2 * (rotationPercent - 0.5)));

            particle.alphaPath(pathFade);

            particle.dieOnSolidsAndLiquids();

            particle.addOnDeathParticles(groundLeafFactory);

            return particle;
        };
    }


    public void setOnGroundFadeTicks(int leafFadeTicks)
    {
        leafFadeTicks = Tools.min(Tools.max(leafFadeTicks, 0), maxAge);
        this.leafFadeTicks = leafFadeTicks;


        double fadeRate = 20d / leafFadeTicks;
        ((CPathConstant) pathFade).position.values[0] = maxAge * fadeRate / 20d;
        ((CPathLinear) pathFadeInternal).motionPerSecond.values[0] = -fadeRate;
    }

    public void setTerminalVelocityMultiplier(double multiplier)
    {
        ((CPathAccelerateToTerminalVel) pathFall).terminalVelocity.values[1] = -0.6 * multiplier;
    }

    public void setTerminalVelocityDelayMultiplier(double multiplier)
    {
        ((CPathAccelerateToTerminalVel) pathFall).timeToTerminalVelocity = (long) (1000 * multiplier);
    }


    @Override
    public PathedParticle create(double x, double y, double z, Object source)
    {
        return fallingLeafFactory.create(x, y, z, source);
    }
}
