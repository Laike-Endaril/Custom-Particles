package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import com.fantasticsource.mctools.blocks.RegistryRegexBlockFilter;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EmitterBlock extends CustomParticleEmitter
{
    private static final BlockPos.MutableBlockPos MUT_POS = new BlockPos.MutableBlockPos();

    public RegistryRegexBlockFilter blockFilter;
    public boolean isWhitelist;


    public EmitterBlock(RegistryRegexBlockFilter filter, boolean isWhitelist)
    {
        this.blockFilter = filter;
        this.isWhitelist = isWhitelist;
    }


    @Override
    public boolean clientTick(int x, int y, int z, Object obj)
    {
        if (!super.clientTick(x, y, z, obj)) return false;


        if (!(obj instanceof IBlockState)) return false;
        if (blockFilter.matches((IBlockState) obj) != isWhitelist) return false;


        World world = Minecraft.getMinecraft().world;
        boolean spawned = false;
        IBlockState adjacent;
        ArrayList<OffsetMode> modeQueue = new ArrayList<>();
        ArrayList<CustomParticleFactory> factoryQueue = new ArrayList<>(factories);
        while (!spawned && factoryQueue.size() > 0)
        {
            CustomParticleFactory factory = Tools.choose(factoryQueue);
            factoryQueue.remove(factory);

            modeQueue.addAll(modes);
            while (!spawned && modeQueue.size() > 0)
            {
                OffsetMode mode = Tools.choose(modes);
                modeQueue.remove(mode);
                switch (mode)
                {
                    case TOP:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).up());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.createInternal(x + Tools.random(1d), y + 1.01, z + Tools.random(1d));
                            spawned = true;
                        }
                        break;

                    case BOTTOM:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).down());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.createInternal(x + Tools.random(1d), y - 0.01, z + Tools.random(1d));
                            spawned = true;
                        }
                        break;

                    case NORTH:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).north());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.createInternal(x + Tools.random(1d), y + Tools.random(1d), z - 0.01);
                            spawned = true;
                        }
                        break;

                    case SOUTH:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).south());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.createInternal(x + Tools.random(1d), y + Tools.random(1d), z + 1.01);
                            spawned = true;
                        }
                        break;

                    case WEST:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).west());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.createInternal(x - 0.01, y + Tools.random(1d), z + Tools.random(1d));
                            spawned = true;
                        }
                        break;

                    case EAST:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).east());
                        if (!adjacent.getMaterial().blocksMovement() || !adjacent.getCollisionBoundingBox(world, MUT_POS).equals(Block.FULL_BLOCK_AABB))
                        {
                            factory.createInternal(x + 1.01, y + Tools.random(1d), z + Tools.random(1d));
                            spawned = true;
                        }
                        break;

                    case INSIDE:
                        factory.createInternal(x + Tools.random(1d), y + Tools.random(1d), z + Tools.random(1d));
                        spawned = true;
                        break;
                }
            }
        }


        return spawned;
    }
}
