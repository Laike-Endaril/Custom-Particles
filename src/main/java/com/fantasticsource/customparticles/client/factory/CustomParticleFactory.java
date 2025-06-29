package com.fantasticsource.customparticles.client.factory;

import com.fantasticsource.customparticles.client.emitter.CustomParticleEmitter;
import com.fantasticsource.customparticles.client.emitter.EmitterBlock;
import com.fantasticsource.mctools.blocks.AdvancedBlockColors;
import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleSharedRenderData;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;

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
    public boolean useFoliageColor = false, useBlockColor = false, useFacingRotation = false;
    public final ArrayList<CPath> motionPaths = new ArrayList<>(), rotationPaths = new ArrayList<>(), rgbPaths = new ArrayList<>(), alphaPaths = new ArrayList<>(), animationPaths = new ArrayList<>();

    protected PathedParticleSharedRenderData particleRenderData = new PathedParticleSharedRenderData(false, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, "textures/particle/particles.png");
    protected int maxAge = 60;
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


    public void setStartingAngle(double minDegrees, double maxDegrees)
    {
        startingRotationMin = -Tools.degtorad(minDegrees);
        startingRotationMax = -Tools.degtorad(maxDegrees);
    }

    public void setMaxSpinRate(double spinRate)
    {
        this.spinRate = Tools.degtorad(Math.abs(spinRate));
    }


    public final void createInternal(double x, double y, double z, CustomParticleEmitter emitter, CPath facingRotationPath, Object source, BlockPos sourcePos)
    {
        PathedParticle particle = create(x, y, z, source);


        particle.cullDistanceSquared = emitter.getCullDistanceSquared();

        particle.spriteMetaData = spriteMetaData;

        particle.positionData.paths.addAll(motionPaths);

        if (rotationPaths.size() > 0 || useFacingRotation)
        {
            ArrayList<CPath> rotations = new ArrayList<>(rotationPaths);
            if (useFacingRotation) rotations.add(facingRotationPath);

            if (particle.rotationData == null)
            {
                CPath.CPathData data = new CPath.CPathData(0);
                data.paths = new ArrayList<>();
                particle.rotationData = data;
            }
            else
            {
                int rotationDims = 1;
                for (CPath path : rotations)
                {
                    if (path.getRelativePosition(0).values.length >= 3)
                    {
                        rotationDims = 3;
                        break;
                    }
                }

                if (particle.rotationData.paths != null && particle.rotationData.paths.size() > 0 && particle.rotationData.paths.get(0).getRelativePosition(0).values.length < rotationDims)
                {
                    particle.rotationData.paths.clear();
                }
            }

            particle.rotationData.paths.addAll(rotations);
        }


        if (useFoliageColor)
        {
            if (particle.rgbData == null)
            {
                particle.rgbData = new CPath.CPathData(0);
                particle.rgbData.paths = new ArrayList<>();
            }

            int c = BiomeColorHelper.getFoliageColorAtPos(Minecraft.getMinecraft().world, sourcePos);
            particle.rgbData.paths.add(new CPathConstant(((c >> 16) & 255) / 255d, ((c >> 8) & 255) / 255d, (c & 255) / 255d));
        }

        if (useBlockColor)
        {
            if (particle.rgbData == null)
            {
                particle.rgbData = new CPath.CPathData(0);
                particle.rgbData.paths = new ArrayList<>();
            }

            IBlockState state = emitter instanceof EmitterBlock ? (IBlockState) source : Minecraft.getMinecraft().world.getBlockState(sourcePos);
            Color c = AdvancedBlockColors.getBlockColor(sourcePos, state);
            particle.rgbData.paths.add(new CPathConstant(c.rf(), c.gf(), c.bf()));
        }

        if (rgbPaths.size() > 0)
        {
            if (particle.rgbData == null)
            {
                particle.rgbData = new CPath.CPathData(0);
                particle.rgbData.paths = new ArrayList<>();
            }

            particle.rgbData.paths.addAll(rgbPaths);
        }


        if (alphaPaths.size() > 0)
        {
            if (particle.alphaData == null)
            {
                particle.alphaData = new CPath.CPathData(0);
                particle.alphaData.paths = new ArrayList<>();
            }

            particle.alphaData.paths.addAll(alphaPaths);
        }

        if (animationPaths.size() > 0)
        {
            if (particle.animationData == null)
            {
                particle.animationData = new CPath.CPathData(0);
                particle.animationData.paths = new ArrayList<>();
            }

            particle.animationData.paths.addAll(animationPaths);
        }
    }

    public abstract PathedParticle create(double x, double y, double z, Object source);
}
