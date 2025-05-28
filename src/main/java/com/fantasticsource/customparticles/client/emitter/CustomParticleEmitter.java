package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.ParticleHandler;
import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public abstract class CustomParticleEmitter
{
    private static final BlockPos.MutableBlockPos MUT_POS = new BlockPos.MutableBlockPos();

    public enum OffsetMode
    {
        TOP,
        BOTTOM,
        NORTH,
        SOUTH,
        WEST,
        EAST,
        INSIDE
    }

    public enum Requirement
    {
        SLIMECHUNK,
        FULLSOLIDBLOCK
    }


    public final ArrayList<CustomParticleFactory> factories = new ArrayList<>();
    public final ArrayList<OffsetMode> modes = new ArrayList<>();
    public final HashSet<Requirement> requirements = new HashSet<>();
    public final HashSet<Integer> dimensions = new HashSet<>();
    public final HashSet<Biome> biomes = new HashSet<>();

    public boolean dimensionsAreWhitelist = true, biomesAreWhitelist = true;
    public int cullDistanceSquared = -1;


    public CustomParticleEmitter()
    {
        ParticleHandler.EMITTERS.computeIfAbsent(getType(), o -> new ArrayList<>()).add(this);
    }


    public void addOffsetModes(OffsetMode... modes)
    {
        this.modes.addAll(Arrays.asList(modes));
    }

    public void addRequirements(Requirement... requirements)
    {
        this.requirements.addAll(Arrays.asList(requirements));
    }

    public void addFactory(CustomParticleFactory factory)
    {
        factories.add(factory);
    }


    public void setCullingDistance(int cullDistance)
    {
        cullDistanceSquared = cullDistance >= (Integer.MAX_VALUE >> 1) ? Integer.MAX_VALUE : cullDistance << 1;
    }


    public final boolean canTriggerMainChecks(int x, int y, int z, Object obj)
    {
        World world = Minecraft.getMinecraft().world;
        if (world == null) return false;


        if (dimensions.size() > 0 && dimensions.contains(world.provider.getDimension()) != dimensionsAreWhitelist) return false;


        MUT_POS.setPos(x, y, z);
        if (biomes.size() > 0 && biomes.contains(world.getBiome(MUT_POS)) != biomesAreWhitelist) return false;


        for (Requirement requirement : requirements)
        {
            switch (requirement)
            {
                case SLIMECHUNK:
                    if (y >= 40 || world.getChunkFromBlockCoords(MUT_POS).getRandomWithSeed(987234911L).nextInt(10) != 0) return false;
                    break;

                case FULLSOLIDBLOCK:
                    IBlockState blockState = (IBlockState) obj;
                    if (!blockState.getMaterial().blocksMovement() || !blockState.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB)) return false;
                    break;
            }
        }

        return true;
    }


    public abstract Class<? extends CustomParticleEmitter> getType();

    public abstract boolean triggerFactories(int x, int y, int z, Object obj);
}
