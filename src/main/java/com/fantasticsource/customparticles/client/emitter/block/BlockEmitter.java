package com.fantasticsource.customparticles.client.emitter.block;

import com.fantasticsource.customparticles.client.emitter.CustomParticleEmitter;
import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import com.fantasticsource.mctools.blocks.RegistryRegexBlockFilter;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockEmitter extends CustomParticleEmitter
{
    public static final BlockPos.MutableBlockPos MUT_POS = new BlockPos.MutableBlockPos();

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


    public RegistryRegexBlockFilter blockFilter;
    public final ArrayList<OffsetMode> modes = new ArrayList<>();


    public BlockEmitter(RegistryRegexBlockFilter filter, OffsetMode... modes)
    {
        this.blockFilter = filter;
        addOffsetModes(modes);
    }

    public void addOffsetModes(OffsetMode... modes)
    {
        this.modes.addAll(Arrays.asList(modes));
    }


    @Override
    public boolean clientTick(int x, int y, int z, Object obj)
    {
        if (!(obj instanceof IBlockState) || !blockFilter.matches((IBlockState) obj)) return false;


        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = Minecraft.getMinecraft().world;
        if (player == null || world == null) return false;


        boolean spawned = false;
        IBlockState adjacent;
        for (CustomParticleFactory factory : factories)
        {
            for (OffsetMode mode : modes)
            {
                switch (mode)
                {
                    case TOP:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).up());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.create(x + Tools.random(1d), y + 1.01, z + Tools.random(1d));
                            spawned = true;
                        }
                        break;

                    case BOTTOM:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).down());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.create(x + Tools.random(1d), y - 0.01, z + Tools.random(1d));
                            spawned = true;
                        }
                        break;

                    case NORTH:
                        factory.create(x + Tools.random(1d), y + Tools.random(1d), z - 0.01);
                        spawned = true;
                        break;

                    case SOUTH:
                        factory.create(x + Tools.random(1d), y + Tools.random(1d), z + 1.01);
                        spawned = true;
                        break;

                    case WEST:
                        factory.create(x - 0.01, y + Tools.random(1d), z + Tools.random(1d));
                        spawned = true;
                        break;

                    case EAST:
                        factory.create(x + 1.01, y + Tools.random(1d), z + Tools.random(1d));
                        spawned = true;
                        break;

                    case INSIDE:
                        factory.create(x + Tools.random(1d), y + Tools.random(1d), z + Tools.random(1d));
                        spawned = true;
                        break;
                }
            }
        }


        return spawned;
    }
}
