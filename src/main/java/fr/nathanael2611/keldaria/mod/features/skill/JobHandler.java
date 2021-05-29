package fr.nathanael2611.keldaria.mod.features.skill;

import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
            if (b != Blocks.GRASS && b != Blocks.FARMLAND && b != Blocks.DIRT && b instanceof IGrowable)
            {
                if (EnumJob.PEASANT.has(player))
                {
                    //TODO: Implement job levels :D
                    if (Helpers.randomInteger(0, 5) > EnumAptitudes.CRAFTING.getPoints(player))
                    {
                        event.getDrops().clear();
                    }
                } else event.getDrops().clear();
            }

        }
    }

}
