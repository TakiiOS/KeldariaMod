package fr.nathanael2611.keldaria.mod.features.skill;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.block.BlockRefinedLog;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.item.ItemRawOre;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class JobHandler
{

    @SubscribeEvent
    public void drops(BlockEvent.HarvestDropsEvent event)
    {
        if (event.getWorld().isRemote) return;
        EntityPlayer player = event.getHarvester();
        if (player != null && player.world != null)
        {
            Block b = event.getState().getBlock();
            if(b instanceof BlockOre)
            {
                BlockPos pos = event.getPos();
                if(EnumJob.MINER.has(player))
                {
                    int level = EnumJob.MINER.getLevel(player).getId();
                    List<ItemStack> nDrops = Lists.newArrayList();
                    nDrops.add(new ItemStack(Blocks.COBBLESTONE));
                    for (ItemStack drop : event.getDrops())
                    {
                        nDrops.add(new ItemStack(ItemRawOre.getRaw(drop.getItem()), Helpers.randomInteger(0, level+1)));
                    }
                    event.getDrops().clear();
                    event.getDrops().addAll(nDrops);
                }
                else
                {
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Blocks.COBBLESTONE));
                    //event.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, 1000, 1000);
                }
            }
            else if(b instanceof BlockNewLog || b instanceof BlockOldLog)
            {
                Item bit = BlockRefinedLog.bit(event.getState());
                Block refined = BlockRefinedLog.refine(event.getState());
                int crafting = EnumAptitudes.CRAFTING.getPoints(player);
                event.getDrops().clear();
                if(EnumJob.LUMBERJACK.has(player))
                {
                    int level = EnumJob.LUMBERJACK.getLevel(player).getId();
                    if(Helpers.randomInteger(0, 3) <= level)
                    {
                        event.getDrops().add(new ItemStack(refined));
                    }
                    event.getDrops().add(new ItemStack(bit, Helpers.randomInteger(0, crafting)));
                }
                else
                {
                    if(Helpers.randomInteger(0, 10) <= crafting)
                    {
                        event.getDrops().add(new ItemStack(bit));
                    }
                }
            }
            else if (b != Blocks.GRASS && b != Blocks.FARMLAND && b != Blocks.DIRT && b instanceof IGrowable)
            {
                if (EnumJob.PEASANT.has(player))
                {
                    if (Helpers.randomInteger(0, 5) > EnumAptitudes.CRAFTING.getPoints(player))
                    {
                        event.getDrops().clear();
                    }
                    else
                    {

                        for (ItemStack drop : event.getDrops())
                        {
                            drop.setCount(Helpers.randomInteger(0, EnumJob.PEASANT.getLevel(player).getId()));
                        }
                    }
                } else event.getDrops().clear();
            }

        }
    }

}
