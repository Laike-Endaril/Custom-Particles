package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleSharedRenderData;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class CustomParticleFactory
{
    public static final LinkedHashMap<String, GlStateManager.SourceFactor> SRC_BLEND_FUNCS = new LinkedHashMap<>();
    public static final LinkedHashMap<String, GlStateManager.DestFactor> DST_BLEND_FUNCS = new LinkedHashMap<>();

    static
    {
        SRC_BLEND_FUNCS.put("GL_ZERO", GlStateManager.SourceFactor.ZERO);
        SRC_BLEND_FUNCS.put("GL_ONE", GlStateManager.SourceFactor.ONE);
        SRC_BLEND_FUNCS.put("GL_SRC_COLOR", GlStateManager.SourceFactor.SRC_COLOR);
        SRC_BLEND_FUNCS.put("GL_ONE_MINUS_SRC_COLOR", GlStateManager.SourceFactor.ONE_MINUS_SRC_COLOR);
        SRC_BLEND_FUNCS.put("GL_DST_COLOR", GlStateManager.SourceFactor.DST_COLOR);
        SRC_BLEND_FUNCS.put("GL_ONE_MINUS_DST_COLOR", GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR);
        SRC_BLEND_FUNCS.put("GL_SRC_ALPHA", GlStateManager.SourceFactor.SRC_ALPHA);
        SRC_BLEND_FUNCS.put("GL_ONE_MINUS_SRC_ALPHA", GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA);
        SRC_BLEND_FUNCS.put("GL_DST_ALPHA", GlStateManager.SourceFactor.DST_ALPHA);
        SRC_BLEND_FUNCS.put("GL_ONE_MINUS_DST_ALPHA", GlStateManager.SourceFactor.ONE_MINUS_DST_ALPHA);
        SRC_BLEND_FUNCS.put("GL_CONSTANT_COLOR", GlStateManager.SourceFactor.CONSTANT_COLOR);
        SRC_BLEND_FUNCS.put("GL_ONE_MINUS_CONSTANT_COLOR", GlStateManager.SourceFactor.ONE_MINUS_CONSTANT_COLOR);
        SRC_BLEND_FUNCS.put("GL_CONSTANT_ALPHA", GlStateManager.SourceFactor.CONSTANT_ALPHA);
        SRC_BLEND_FUNCS.put("GL_ONE_MINUS_CONSTANT_ALPHA", GlStateManager.SourceFactor.ONE_MINUS_CONSTANT_ALPHA);
        SRC_BLEND_FUNCS.put("GL_SRC_ALPHA_SATURATE", GlStateManager.SourceFactor.SRC_ALPHA_SATURATE);

        DST_BLEND_FUNCS.put("GL_ZERO", GlStateManager.DestFactor.ZERO);
        DST_BLEND_FUNCS.put("GL_ONE", GlStateManager.DestFactor.ONE);
        DST_BLEND_FUNCS.put("GL_SRC_COLOR", GlStateManager.DestFactor.SRC_COLOR);
        DST_BLEND_FUNCS.put("GL_ONE_MINUS_SRC_COLOR", GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        DST_BLEND_FUNCS.put("GL_DST_COLOR", GlStateManager.DestFactor.DST_COLOR);
        DST_BLEND_FUNCS.put("GL_ONE_MINUS_DST_COLOR", GlStateManager.DestFactor.ONE_MINUS_DST_COLOR);
        DST_BLEND_FUNCS.put("GL_SRC_ALPHA", GlStateManager.DestFactor.SRC_ALPHA);
        DST_BLEND_FUNCS.put("GL_ONE_MINUS_SRC_ALPHA", GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        DST_BLEND_FUNCS.put("GL_DST_ALPHA", GlStateManager.DestFactor.DST_ALPHA);
        DST_BLEND_FUNCS.put("GL_ONE_MINUS_DST_ALPHA", GlStateManager.DestFactor.ONE_MINUS_DST_ALPHA);
        DST_BLEND_FUNCS.put("GL_CONSTANT_COLOR", GlStateManager.DestFactor.CONSTANT_COLOR);
        DST_BLEND_FUNCS.put("GL_ONE_MINUS_CONSTANT_COLOR", GlStateManager.DestFactor.ONE_MINUS_CONSTANT_COLOR);
        DST_BLEND_FUNCS.put("GL_CONSTANT_ALPHA", GlStateManager.DestFactor.CONSTANT_ALPHA);
        DST_BLEND_FUNCS.put("GL_ONE_MINUS_CONSTANT_ALPHA", GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
    }


    public SpriteMetaData spriteMetaData;
    public boolean useFoliageColor = false;
    public final ArrayList<CPath> positionPaths = new ArrayList<>(), rotationPaths = new ArrayList<>();

    protected PathedParticleSharedRenderData particleRenderData = new PathedParticleSharedRenderData(false, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, "textures/particle/particles.png");
    protected int cullDistanceSquared = -1;
    protected double startingRotationMin, startingRotationMax, spinRate;


    public void useBlockLight(boolean useBlockLight)
    {
        particleRenderData = new PathedParticleSharedRenderData(useBlockLight, particleRenderData.sourceFactor, particleRenderData.destFactor, particleRenderData.textureString);
    }

    public void setBlendSourceFactor(String sourceFactor)
    {
        GlStateManager.SourceFactor result = SRC_BLEND_FUNCS.get(sourceFactor);
        if (result == null) throw new IllegalArgumentException("Bad source factor: " + sourceFactor + " (see https://registry.khronos.org/OpenGL-Refpages/gl4/html/glBlendFunc.xhtml)");

        particleRenderData = new PathedParticleSharedRenderData(particleRenderData.useBlockLight, result, particleRenderData.destFactor, particleRenderData.textureString);
    }

    public void setBlendDestinationFactor(String destinationFactor)
    {
        GlStateManager.DestFactor result = DST_BLEND_FUNCS.get(destinationFactor);
        if (result == null) throw new IllegalArgumentException("Bad destination factor: " + destinationFactor + " (see https://registry.khronos.org/OpenGL-Refpages/gl4/html/glBlendFunc.xhtml)");

        particleRenderData = new PathedParticleSharedRenderData(particleRenderData.useBlockLight, particleRenderData.sourceFactor, result, particleRenderData.textureString);
    }

    public void setTexture(String texture)
    {
        particleRenderData = new PathedParticleSharedRenderData(particleRenderData.useBlockLight, particleRenderData.sourceFactor, particleRenderData.destFactor, texture);
    }


    public void setCullingDistance(int cullDistance)
    {
        cullDistanceSquared = cullDistance >= (Integer.MAX_VALUE >> 1) ? Integer.MAX_VALUE : cullDistance << 1;
    }


    public void setStartingAngle(double minDegrees, double maxDegrees)
    {
        startingRotationMin = -Tools.degtorad(minDegrees);
        startingRotationMax = -Tools.degtorad(maxDegrees);
    }

    public void setMaxSpinRate(double spinRate)
    {
        this.spinRate = Tools.degtorad(Math.abs(spinRate));
    }


    public final void createInternal(double x, double y, double z)
    {
        PathedParticle particle = create(x, y, z);

        if (cullDistanceSquared != -1) particle.cullDistanceSquared = cullDistanceSquared;
        particle.positionData.paths.addAll(positionPaths);
        particle.rotationData.paths.addAll(rotationPaths);
    }

    public abstract PathedParticle create(double x, double y, double z);
}
