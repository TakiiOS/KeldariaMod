/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public class Populator
{

    public static enum FillType
    {
        SURFACE, INSIDE;

        public static FillType byName(String str)
        {
            if(str.equalsIgnoreCase("surface")) return SURFACE;
            else if(str.equalsIgnoreCase("inside")) return INSIDE;
            else return null;
        }
    }

    public static enum PopulateType
    {
        SIMPLE, ORE;

        public List<BlockPos> getBlocksToFill(BlockPos pos)
        {
            if(this == SIMPLE)
            {
                return Lists.newArrayList(pos);
            }
            else
            {
                List<BlockPos> poses = Lists.newArrayList();

                for (EnumFacing value : EnumFacing.values())
                {
                    if(Helpers.randomDouble(0, 100) > 60)
                    {
                        BlockPos p = pos.offset(value);
                        poses.add(p);
                        int i = (int) Helpers.randomDouble(0, 100);
                        if(i > 80)
                        {
                            poses.addAll(getBlocksToFill(p));
                        }
                    }
                }
                return poses;
            }
        }

        public static PopulateType byName(String str)
        {
            if(str.equalsIgnoreCase("simple")) return SIMPLE;
            else if(str.equalsIgnoreCase("ore")) return ORE;
            else return null;
        }
    }

    public static void populate(Chunk chunk, FillType fillType, PopulateType populateType, List<Block> mask, Block block, double percentChance, int minY, int maxY)
    {
        //Chunk chunk = world.getChunk(chunkX, chunkY);
        World world = chunk.getWorld();
        List<BlockPos> posesToFill = Lists.newArrayList();
        if(fillType == FillType.INSIDE)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    for (int i = maxY; i > minY; i--)
                    {
                        BlockPos pos = new BlockPos((chunk.x * 16) + x, i, (chunk.z * 16) + z);
                        List<BlockPos> poses = populateType.getBlocksToFill(pos);
                        double r = Helpers.randomDouble(0, 100);
                        if(r <= percentChance)
                        {
                            for (BlockPos pose : poses)
                            {
                                IBlockState state = world.getBlockState(pose);
                                if (mask.contains(state.getBlock()))
                                {
                                    posesToFill.add(pose);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (BlockPos pos : posesToFill)
        {
            world.setBlockState(pos, block.getDefaultState(), 3);
        }
    }


}
