package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import com.fantasticsource.mctools.ClientTickTimer;
import com.fantasticsource.mctools.blocks.AdvancedBlockFilter;
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

    public EmitterWallFall(AdvancedBlockFilter filter, boolean isWhitelist, int gridWidthPerBlock, int gridHeightPerBlock, double secondsPerMove)
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
    public boolean triggerFactories(int x, int y, int z, Object source)
    {
        if (!canTriggerMainChecks(x, y, z, source)) return false;


        if (!(source instanceof IBlockState)) return false;
        if (blockFilter.matches((IBlockState) source) != isWhitelist) return false;


        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.world;
        IBlockState stateAbove = world.getBlockState(new BlockPos(x, y + 1, z));
        boolean additionalChecks = blockFilter.matches(stateAbove) == isWhitelist; //If the block above was valid for spawning not accounting for horizontal adjacents, do checks on above adjacents for face-based spawns
        boolean good;


        boolean spawned = false;
        IBlockState adjacent, aboveAdjacent;
        ArrayList<OffsetMode> modeQueue = new ArrayList<>();
        ArrayList<CustomParticleFactory> factoryQueue = new ArrayList<>(factories);
        BlockPos sourcePos = new BlockPos(x, y, z);
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
                                minecraft.addScheduledTask(() -> triggerRecursive(world, factory, x + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), y + 1 - halfGridSectorH, z - 0.01, x, z, FACING_ROTATION_NORTH, source, sourcePos));
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
                                minecraft.addScheduledTask(() -> triggerRecursive(world, factory, x + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), y + 1 - halfGridSectorH, z + 1.01, x, z, FACING_ROTATION_SOUTH, source, sourcePos));
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
                                minecraft.addScheduledTask(() -> triggerRecursive(world, factory, x - 0.01, y + 1 - halfGridSectorH, z + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), x, z, FACING_ROTATION_WEST, source, sourcePos));
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
                                minecraft.addScheduledTask(() -> triggerRecursive(world, factory, x + 1.01, y + 1 - halfGridSectorH, z + halfGridSectorW + gridSectorW * Tools.random(gridWidthPerBlock), x, z, FACING_ROTATION_EAST, source, sourcePos));
                                spawned = true;
                            }
                        }
                        break;
                }
            }
        }


        return spawned;
    }


    protected void triggerRecursive(World world, CustomParticleFactory factory, double x, double y, double z, int mainColumnX, int mainColumnZ, CPath facingRotation, Object source, BlockPos sourcePos)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (world != minecraft.world) return;

        if (!canTriggerMainChecks(mainColumnX, (int) y, mainColumnZ, source)) return;
        if (blockFilter.matches((IBlockState) source) != isWhitelist) return;

        IBlockState adjacent = world.getBlockState(MUT_POS.setPos(x, y, z));
        if (adjacent.getMaterial().blocksMovement() && Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS))) return;


        trySpawn(x, y, z, minecraft, minecraft.player, factory, facingRotation, source, sourcePos);


        double yy = y - gridSectorH;
        BlockPos nextSourcePos;
        Object nextSource;
        if ((int) y != yy)
        {
            nextSourcePos = new BlockPos(mainColumnX, yy, mainColumnZ);
            nextSource = world.getBlockState(nextSourcePos);
        }
        else
        {
            nextSourcePos = sourcePos;
            nextSource = source;
        }
        ClientTickTimer.schedule(ticksPerMove, () -> triggerRecursive(world, factory, x, yy, z, mainColumnX, mainColumnZ, facingRotation, nextSource, nextSourcePos));
    }
}
