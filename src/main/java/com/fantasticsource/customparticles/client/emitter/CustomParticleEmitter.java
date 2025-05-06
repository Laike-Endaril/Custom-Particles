package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.ParticleHandler;
import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        SLIME_CHUNK,
        FULL_SOLID_BLOCK
    }


    public final ArrayList<CustomParticleFactory> factories = new ArrayList<>();
    public final ArrayList<OffsetMode> modes = new ArrayList<>();
    public final HashSet<Requirement> requirements = new HashSet<>();


    public CustomParticleEmitter()
    {
        ParticleHandler.EMITTERS.computeIfAbsent(getClass(), o -> new ArrayList<>()).add(this);
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


    public boolean clientTick(int x, int y, int z, Object obj)
    {
        World world = Minecraft.getMinecraft().world;
        if (world == null) return false;


        for (Requirement requirement : requirements)
        {
            switch (requirement)
            {
                case SLIME_CHUNK:
                    if (y >= 40 || world.getChunkFromBlockCoords(MUT_POS.setPos(x, y, z)).getRandomWithSeed(987234911L).nextInt(10) != 0) return false;
                    break;

                case FULL_SOLID_BLOCK:
                    IBlockState blockState = world.getBlockState(MUT_POS.setPos(x, y, z));
                    if (!blockState.getMaterial().blocksMovement() || !blockState.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB)) return false;
                    break;
            }
        }

        return true;
    }
}
