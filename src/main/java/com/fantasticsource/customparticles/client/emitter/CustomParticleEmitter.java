package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.ParticleHandler;
import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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
        FULLSOLIDBLOCK,
        WEATHERCLEAR,
        WEATHERRAIN,
        WEATHERTHUNDER
    }


    public final ArrayList<CustomParticleFactory> factories = new ArrayList<>();
    public final ArrayList<OffsetMode> modes = new ArrayList<>();
    public final HashSet<Requirement> requirements = new HashSet<>();
    public final HashSet<Integer> dimensions = new HashSet<>();
    public final HashSet<Biome> biomes = new HashSet<>();

    public boolean dimensionsAreWhitelist = true, biomesAreWhitelist = true;
    public double density = 100;

    protected double cullDistance = 30, cullDistanceSquared = 900, cullDistanceCubed = 27000,
            maxSpawnDistance = 20, maxSpawnDistanceSquared = 400, maxSpawnDistanceCubed = 8000;


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


    public void setCullingDistance(double cullDistance)
    {
        if (cullDistance <= 0) throw new IllegalArgumentException("Cull distance must be greater than 0!");

        this.cullDistance = cullDistance;
        cullDistanceSquared = cullDistance * cullDistance;
        cullDistanceCubed = cullDistanceSquared * cullDistance;
    }

    public double getCullDistanceSquared()
    {
        return cullDistanceSquared;
    }


    public void setMaxSpawnDistance(double maxSpawnDistance)
    {
        if (maxSpawnDistance <= 0) throw new IllegalArgumentException("Max spawn distance must be greater than 0!");

        this.maxSpawnDistance = maxSpawnDistance;
        maxSpawnDistanceSquared = maxSpawnDistance * maxSpawnDistance;
        maxSpawnDistanceCubed = maxSpawnDistanceSquared * maxSpawnDistance;
    }

    public double getMaxSpawnDistance()
    {
        return Tools.min(maxSpawnDistance, cullDistance);
    }

    public double getMaxSpawnDistanceSquared()
    {
        return Tools.min(maxSpawnDistanceSquared, cullDistanceSquared);
    }

    public double getMaxSpawnDistanceCubed()
    {
        return Tools.min(maxSpawnDistanceCubed, cullDistanceCubed);
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
                    if (!blockState.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(blockState.getCollisionBoundingBox(world, MUT_POS))) return false;
                    break;

                case WEATHERCLEAR:
                    if (world.isRaining() || world.isThundering()) return false;
                    break;

                case WEATHERRAIN:
                    if (!world.isRaining()) return false;
                    break;

                case WEATHERTHUNDER:
                    if (!world.isThundering()) return false;
                    break;
            }
        }

        return true;
    }


    public abstract Class<? extends CustomParticleEmitter> getType();

    public abstract boolean triggerFactories(int x, int y, int z, Object source);

    public boolean trySpawn(double x, double y, double z, Minecraft minecraft, EntityPlayer player, CustomParticleFactory factory, CPath facingRotationPath, Object source, BlockPos sourcePos)
    {
        if (player.getPositionVector().squareDistanceTo(x, y - player.eyeHeight, z) > getMaxSpawnDistanceSquared()) return false;

        if (Thread.currentThread().getName().equals("Client thread")) factory.createInternal(x, y, z, this, facingRotationPath, source, sourcePos);
        else minecraft.addScheduledTask(() -> factory.createInternal(x, y, z, this, facingRotationPath, source, sourcePos));
        return true;
    }
}
