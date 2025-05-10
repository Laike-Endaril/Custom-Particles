package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import com.fantasticsource.mctools.ClientTickTimer;
import com.fantasticsource.mctools.blocks.RegistryRegexBlockFilter;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EmitterWallFall extends EmitterBlock
{
    public final int gridWidthPerBlock, gridHeightPerBlock, ticksPerMove;
    public final double gridSectorW, gridSectorH, halfGridSectorW, halfGridSectorH;

    public EmitterWallFall(RegistryRegexBlockFilter filter, boolean isWhitelist, int gridWidthPerBlock, int gridHeightPerBlock, double secondsPerMove)
    {
        //TODO make this thing move in a direction until the block behind it is one it's not allowed on via the block filter
        //TODO along that path, make it periodically spawn runes in its wake
        //TODO make the green runes with this
        super(filter, isWhitelist);
        this.gridWidthPerBlock = gridWidthPerBlock;
        this.gridHeightPerBlock = gridHeightPerBlock;
        gridSectorW = 1d / gridWidthPerBlock;
        gridSectorH = 1d / gridHeightPerBlock;
        halfGridSectorW = gridSectorW * 0.5;
        halfGridSectorH = gridSectorH * 0.5;
        ticksPerMove = (int) (secondsPerMove * 20);
    }

    @Override
    public boolean triggerFactories(int x, int y, int z, Object obj)
    {
        if (!canTriggerMainChecks(x, y, z)) return false;


        if (!(obj instanceof IBlockState)) return false;
        if (blockFilter.matches((IBlockState) obj) != isWhitelist) return false;


        IBlockState stateAbove = Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y + 1, z));
        boolean additionalChecks = blockFilter.matches(stateAbove) == isWhitelist; //If the block above was valid for spawning not accounting for horizontal adjacents, do checks on above adjacents for face-based spawns
        boolean good;


        World world = Minecraft.getMinecraft().world;
        boolean spawned = false;
        IBlockState adjacent, aboveAdjacent;
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
                    case NORTH:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z - 1));
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            good = !additionalChecks;
                            if (!good)
                            {
                                aboveAdjacent = world.getBlockState(MUT_POS.setPos(MUT_POS.getX(), MUT_POS.getY() + 1, MUT_POS.getZ()));
                                good = aboveAdjacent.getMaterial().blocksMovement() && Block.FULL_BLOCK_AABB.equals(aboveAdjacent.getCollisionBoundingBox(world, MUT_POS));
                            }
                            if (good)
                            {
                                triggerRecursive(world, factory, x + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), y + 1 - halfGridSectorH, z - 0.01, x, z, FACING_ROTATION_NORTH);
                                spawned = true;
                            }
                        }
                        break;

                    case SOUTH:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z + 1));
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            good = !additionalChecks;
                            if (!good)
                            {
                                aboveAdjacent = world.getBlockState(MUT_POS.setPos(MUT_POS.getX(), MUT_POS.getY() + 1, MUT_POS.getZ()));
                                good = aboveAdjacent.getMaterial().blocksMovement() && Block.FULL_BLOCK_AABB.equals(aboveAdjacent.getCollisionBoundingBox(world, MUT_POS));
                            }
                            if (good)
                            {
                                triggerRecursive(world, factory, x + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), y + 1 - halfGridSectorH, z + 1.01, x, z, FACING_ROTATION_SOUTH);
                                spawned = true;
                            }
                        }
                        break;

                    case WEST:
                        adjacent = world.getBlockState(MUT_POS.setPos(x - 1, y, z));
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            good = !additionalChecks;
                            if (!good)
                            {
                                aboveAdjacent = world.getBlockState(MUT_POS.setPos(MUT_POS.getX(), MUT_POS.getY() + 1, MUT_POS.getZ()));
                                good = aboveAdjacent.getMaterial().blocksMovement() && Block.FULL_BLOCK_AABB.equals(aboveAdjacent.getCollisionBoundingBox(world, MUT_POS));
                            }
                            if (good)
                            {
                                triggerRecursive(world, factory, x - 0.01, y + 1 - halfGridSectorH, z + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), x, z, FACING_ROTATION_WEST);
                                spawned = true;
                            }
                        }
                        break;

                    case EAST:
                        adjacent = world.getBlockState(MUT_POS.setPos(x + 1, y, z));
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            good = !additionalChecks;
                            if (!good)
                            {
                                aboveAdjacent = world.getBlockState(MUT_POS.setPos(MUT_POS.getX(), MUT_POS.getY() + 1, MUT_POS.getZ()));
                                good = aboveAdjacent.getMaterial().blocksMovement() && Block.FULL_BLOCK_AABB.equals(aboveAdjacent.getCollisionBoundingBox(world, MUT_POS));
                            }
                            if (good)
                            {
                                triggerRecursive(world, factory, x + 1.01, y + 1 - halfGridSectorH, z + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), x, z, FACING_ROTATION_EAST);
                                spawned = true;
                            }
                        }
                        break;
                }
            }
        }


        return spawned;
    }


    protected void triggerRecursive(World world, CustomParticleFactory factory, double x, double y, double z, int mainColumnX, int mainColumnZ, CPath facingRotation)
    {
        if (world != Minecraft.getMinecraft().world) return;
        if (!canTriggerMainChecks(mainColumnX, (int) y, mainColumnZ)) return;
        if (blockFilter.matches(world.getBlockState(MUT_POS.setPos(mainColumnX, y, mainColumnZ))) != isWhitelist) return;

        IBlockState adjacent = world.getBlockState(MUT_POS.setPos(x, y, z));
        if (adjacent.getMaterial().blocksMovement() && Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS))) return;


        factory.createInternal(x, y, z, this, facingRotation);
        ClientTickTimer.schedule(ticksPerMove, () -> triggerRecursive(world, factory, x, y - gridSectorH, z, mainColumnX, mainColumnZ, facingRotation));
    }
}
