package com.fantasticsource.customparticles.client.emitter;

import com.fantasticsource.customparticles.client.factory.CustomParticleFactory;
import com.fantasticsource.mctools.blocks.AdvancedBlockFilter;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathConstant;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EmitterBlock extends CustomParticleEmitter
{
    protected static final BlockPos.MutableBlockPos MUT_POS = new BlockPos.MutableBlockPos();
    protected static final CPath
            FACING_ROTATION_NORTH = new CPathConstant(0, 0, 0),
            FACING_ROTATION_WEST = new CPathConstant(Math.PI * 0.5, 0, 0),
            FACING_ROTATION_SOUTH = new CPathConstant(Math.PI, 0, 0),
            FACING_ROTATION_EAST = new CPathConstant(Math.PI * 1.5, 0, 0),
            FACING_ROTATION_TOP = new CPathConstant(Math.PI, Math.PI * 0.5, 0),
            FACING_ROTATION_BOTTOM = new CPathConstant(Math.PI, -Math.PI * 0.5, 0);


    public AdvancedBlockFilter blockFilter;
    public boolean isWhitelist;


    public EmitterBlock(AdvancedBlockFilter filter, boolean isWhitelist)
    {
        this.blockFilter = filter;
        this.isWhitelist = isWhitelist;
    }


    @Override
    public Class<? extends CustomParticleEmitter> getType()
    {
        return EmitterBlock.class;
    }

    @Override
    public boolean triggerFactories(int x, int y, int z, Object source)
    {
        if (!canTriggerMainChecks(x, y, z, source)) return false;


        if (!(source instanceof IBlockState)) return false;
        if (blockFilter.matches((IBlockState) source) != isWhitelist) return false;


        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.world;
        EntityPlayer player = minecraft.player;
        if (world == null || player == null) return false;


        boolean spawned = false;
        IBlockState adjacent;
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
                    case TOP:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).up());
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            if (trySpawn(x + Tools.random(), y + 1.01, z + Tools.random(), minecraft, player, factory, FACING_ROTATION_TOP, source, sourcePos)) spawned = true;
                        }
                        break;

                    case BOTTOM:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).down());
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            if (trySpawn(x + Tools.random(), y - 0.01, z + Tools.random(), minecraft, player, factory, FACING_ROTATION_BOTTOM, source, sourcePos)) spawned = true;
                        }
                        break;

                    case NORTH:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).north());
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            if (trySpawn(x + Tools.random(), y + Tools.random(), z - 0.01, minecraft, player, factory, FACING_ROTATION_NORTH, source, sourcePos)) spawned = true;
                        }
                        break;

                    case SOUTH:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).south());
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            if (trySpawn(x + Tools.random(), y + Tools.random(), z + 1.01, minecraft, player, factory, FACING_ROTATION_SOUTH, source, sourcePos)) spawned = true;
                        }
                        break;

                    case WEST:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).west());
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            if (trySpawn(x - 0.01, y + Tools.random(), z + Tools.random(), minecraft, player, factory, FACING_ROTATION_WEST, source, sourcePos)) spawned = true;
                        }
                        break;

                    case EAST:
                        adjacent = world.getBlockState(MUT_POS.setPos(x, y, z).east());
                        if (!adjacent.getMaterial().blocksMovement() || !Block.FULL_BLOCK_AABB.equals(adjacent.getCollisionBoundingBox(world, MUT_POS)))
                        {
                            if (trySpawn(x + 1.01, y + Tools.random(), z + Tools.random(), minecraft, player, factory, FACING_ROTATION_EAST, source, sourcePos)) spawned = true;
                        }
                        break;

                    case INSIDE:
                        if (trySpawn(x + Tools.random(), y + Tools.random(), z + Tools.random(), minecraft, player, factory, FACING_ROTATION_NORTH, source, sourcePos)) spawned = true;
                        break;
                }
            }
        }


        return spawned;
    }


}
